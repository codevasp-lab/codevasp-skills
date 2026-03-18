package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/agl/ed25519/extra25519"
	crypto "github.com/codevasp-lab/go-example/utils"
	"io"
	"net/http"
	"strconv"
	"testing"
	"time"
)

func TestAssetAddressSearch(t *testing.T) {

	// Convert keys to base64 for easier representation
	base64OwnPrivateKey := "5f6cGTssbOoQ1i41K3OU1WD4EwGPBf9tYS4wJeFSWjE="
	base64RemotePublicKey := "8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y="

	// Test data
	message := map[string]interface{}{
		"ivms101": map[string]interface{}{
			"Beneficiary": map[string]interface{}{
				"beneficiaryPersons": []interface{}{},
				"accountNumber":      []string{"1KzHK8WMRHRCvRjUV5PFny3v6fqT3UAY5K"},
			},
		},
	}

	jsonData, err := json.Marshal(message)
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

	// Create body with encrypted payload
	requestBody := map[string]string{
		"currency": "BTC",
		"payload":  base64.StdEncoding.EncodeToString(encryptedMessage),
	}
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
	url := fmt.Sprintf("https://trapi-dev.codevasp.com/v1/code/VerifyAddress/codexchange-non-kor")

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

}
