package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/agl/ed25519/extra25519"
	crypto "github.com/codevasp-lab/go-example/utils"
	"github.com/google/uuid"
	"io"
	"net/http"
	"strconv"
	"testing"
	"time"
)

func TestAssetTransferAuthorization(t *testing.T) {

	// Convert keys to base64 for easier representation
	base64OwnPrivateKey := "5f6cGTssbOoQ1i41K3OU1WD4EwGPBf9tYS4wJeFSWjE="
	base64RemotePublicKey := "8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y="

	// Test data
	payload := generateIvmsPayload(
		"Robert",
		"Barnes",
		"",
		"로버트 반스",
		"UID1234567890",
		"1FfmbHfnpaZjKFvyi1okTjJJusN455paPH",
		"code",
		"kim",
		"",
		"김코드",
		"UID0987654321",
		"1KzHK8WMRHRCvRjUV5PFny3v6fqT3UAY5K",
	)

	jsonData, err := json.Marshal(payload)
	jsonString := string(jsonData)
	fmt.Println("payload:")
	fmt.Println(jsonString)

	senderPrivateKey, _ := base64.StdEncoding.DecodeString(base64OwnPrivateKey)
	senderPrivateKey = crypto.PaddingShift(senderPrivateKey, 64)
	receiverPublicKey, _ := base64.StdEncoding.DecodeString(base64RemotePublicKey)

	// Convert the ed25519 private key to a curve25519 private key
	// Curve25519 is only for encryption
	var curve25519SenderPrivateKey [32]byte
	extra25519.PrivateKeyToCurve25519(&curve25519SenderPrivateKey, (*[64]byte)(senderPrivateKey))
	var curve25519ReceiverPublicKey [32]byte
	extra25519.PublicKeyToCurve25519(&curve25519ReceiverPublicKey, (*[32]byte)(receiverPublicKey))

	// create new request
	newRequest, err := crypto.Sodium(base64.StdEncoding.EncodeToString(curve25519SenderPrivateKey[:]), base64.StdEncoding.EncodeToString(curve25519ReceiverPublicKey[:]))
	if err != nil {
		panic(err)
	}

	// Actual Encryption
	encryptedMessage, err := newRequest.Encrypt(jsonString)
	if err != nil {
		panic(err)
	}
	fmt.Println("encryptedMessage")
	fmt.Println(base64.StdEncoding.EncodeToString(encryptedMessage))

	transferID := uuid.New()
	// Create body with encrypted payload
	requestBody := make(map[string]interface{})
	generateRequestBody(
		requestBody,
		transferID.String(),
		"BTC",
		"1",
		"65000",
		"USD",
		"true",
	)
	requestBody["payload"] = base64.StdEncoding.EncodeToString(encryptedMessage)

	requestBodyJSON, err := json.Marshal(requestBody)
	requestBodyJsonString := string(requestBodyJSON)
	fmt.Println("==========")
	fmt.Println("requestBodyJsonString:")
	fmt.Println(requestBodyJsonString)

	// create signature
	signature, err := crypto.GenerateSignature(requestBodyJsonString, base64OwnPrivateKey)

	fmt.Printf("Signature: %s\n", signature.Signature)

	base64OwnPublicKey, err := crypto.GetPublicKey(base64OwnPrivateKey)
	fmt.Println("==========")
	fmt.Println("base64OwnPublicKey:")
	fmt.Println(base64OwnPublicKey)

	// Create the header
	header := http.Header{
		"X-Code-Req-PubKey":        {base64OwnPublicKey},
		"X-Code-Req-Signature":     {signature.Signature},
		"X-Request-Origin":         {"code:aasdasda1aa22a2a-aa"},
		"X-Code-Req-Datetime":      {signature.DateTime},
		"X-Code-Req-Nonce":         {strconv.Itoa(signature.Nonce)},
		"X-Code-Req-Remote-PubKey": {base64RemotePublicKey},
		"Content-Type":             {"application/json"},
	}

	// URL address
	url := fmt.Sprintf("https://trapi-dev.codevasp.com/v1/code/transfer/codexchange-non-kor")

	// Create a new request
	req, err := http.NewRequest(http.MethodPost, url, bytes.NewBuffer(requestBodyJSON))
	if err != nil {
		fmt.Printf("Error creating request: %v\n", err)
		return
	}

	// Set the headers
	req.Header = header

	// Create a client and send the request
	client := &http.Client{Timeout: time.Second * 10}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("Error sending request: %v\n", err)
		return
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			fmt.Printf("Error sending request: %v\n", err)
		}
	}(resp.Body)

	// Read the response
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("Error reading response: %v\n", err)
		return
	}

	// Print the results
	fmt.Println("==========")
	fmt.Println("results:")
	fmt.Println(resp.StatusCode)
	fmt.Println(string(body))

	var receivedMessage map[string]interface{}
	if err := json.Unmarshal(body, &receivedMessage); err != nil {
		fmt.Printf("Error unmarshalling response: %v\n", err)
		fmt.Println(string(body))
	} else {
		if payload, ok := receivedMessage["payload"].(string); ok {
			decodedPayload, _ := base64.StdEncoding.DecodeString(payload)
			// Initialize receiver's crypto struct
			newResponse, err := crypto.Sodium(base64.StdEncoding.EncodeToString(curve25519SenderPrivateKey[:]), base64.StdEncoding.EncodeToString(curve25519ReceiverPublicKey[:]))
			if err != nil {
				panic(err)
			}
			fmt.Println("=====================")
			decrypted, err := newResponse.Decrypt(decodedPayload)
			if err != nil {
				fmt.Printf("Error decrypting payload: %v\n", err)
			} else {
				var objPayload interface{}
				if err := json.Unmarshal(decrypted, &objPayload); err != nil {
					fmt.Printf("Error unmarshalling decrypted payload: %v\n", err)
				} else {
					receivedMessage["payload"] = objPayload
					fmt.Printf("response payload: %v\n", objPayload)
					prettyJSON, _ := json.MarshalIndent(objPayload, "", "  ")
					fmt.Println("Decrypted Message:")
					fmt.Println(string(prettyJSON))
				}
			}
		}

	}

}

func generateRequestBody(
	codeMessage map[string]interface{},
	uuid string,
	currency, amount, tradePrice, tradeCurrency, isExceedingThreshold string,
) {
	codeMessage["transferId"] = uuid
	codeMessage["currency"] = currency
	codeMessage["amount"] = amount
	codeMessage["historicalCost"] = ""
	codeMessage["tradePrice"] = tradePrice
	codeMessage["tradeCurrency"] = tradeCurrency
	codeMessage["isExceedingThreshold"] = isExceedingThreshold
}

func generateIvmsPayload(
	originFirstName, originLastName, originLocalFirstName, originLocalLastName, originId, originAddress,
	beneficiaryFirstName, beneficiaryLastName, beneficiaryLocalFirstName, beneficiaryLocalLastName, beneficiaryId, beneficiaryAddress string,
) map[string]interface{} {
	// Originator
	originator := map[string]interface{}{
		"originatorPersons": []interface{}{
			map[string]interface{}{
				"naturalPerson": map[string]interface{}{
					"name": map[string]interface{}{
						"nameIdentifier": []interface{}{
							map[string]string{
								"primaryIdentifier":   originLastName,
								"secondaryIdentifier": originFirstName,
								"nameIdentifierType":  "LEGL",
							},
						},
						"localNameIdentifier": []interface{}{
							map[string]string{
								"primaryIdentifier":   originLocalLastName,
								"secondaryIdentifier": originLocalFirstName,
								"nameIdentifierType":  "LEGL",
							},
						},
					},
					"customerIdentification": originId,
				},
			},
		},
		"accountNumber": []string{originAddress},
	}

	// Beneficiary
	beneficiary := map[string]interface{}{
		"beneficiaryPersons": []interface{}{
			map[string]interface{}{
				"naturalPerson": map[string]interface{}{
					"name": map[string]interface{}{
						"nameIdentifier": []interface{}{
							map[string]string{
								"primaryIdentifier":   beneficiaryLastName,
								"secondaryIdentifier": beneficiaryFirstName,
								"nameIdentifierType":  "LEGL",
							},
						},
						"localNameIdentifier": []interface{}{
							map[string]string{
								"primaryIdentifier":   beneficiaryLocalLastName,
								"secondaryIdentifier": beneficiaryLocalFirstName,
								"nameIdentifierType":  "LEGL",
							},
						},
					},
					"customerIdentification": beneficiaryId,
				},
			},
		},
		"accountNumber": []string{beneficiaryAddress},
	}

	// Originating VASP
	originatingVASP := map[string]interface{}{
		"originatingVASP": map[string]interface{}{
			"legalPerson": map[string]interface{}{
				"name": map[string]interface{}{
					"nameIdentifier": []interface{}{
						map[string]string{
							"legalPersonName":               "Test 5 entity",
							"legalPersonNameIdentifierType": "LEGL",
						},
					},
				},
				"geographicAddress": []interface{}{
					map[string]interface{}{
						"addressType": "GEOG",
						"addressLine": []string{
							"100 Teheran-ro 1-gil",
							"Gangnam-gu",
							"10th floor",
						},
						"country": "DZ",
					},
				},
				"countryOfRegistration": "DZ",
			},
		},
	}

	// Combine all parts
	ivms := map[string]interface{}{
		"Originator":      originator,
		"Beneficiary":     beneficiary,
		"OriginatingVASP": originatingVASP,
	}

	return map[string]interface{}{
		"ivms101": ivms,
	}
}
