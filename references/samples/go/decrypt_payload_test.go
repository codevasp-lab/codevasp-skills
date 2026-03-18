package main

import (
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/agl/ed25519/extra25519"
	crypto "github.com/codevasp-lab/go-example/utils"
	"testing"
)

func TestDecryptPayload(t *testing.T) {
	ownPrivateKey := "HaFGZdcmhEWrbDO+jq1lrWvMwBLoiJf99Bm7FfMkad0="
	reqPubkey := "3sR8zw8hPsedB1u4lj0qStyvboFAltf9DakLL68CTIw="
	payload := "XUpdV8WnvjqoHEFe3COtIM+muV19aGaOetaM5BEFrTFHkEgO5ZKLZDno+RC8XVfvfhabS92RzQIMMDblI2f9Kk0Ktq5qXGnS/coHghx8JZi+lI211sAdiIZGJZgHHJZdXh6vYIQW2tg7s/WA9qDZsfYDCQ8/k9voMvWBfUI7EPRTXE3JPJMlsroEEj5Yw1y4wcg1cQ=="

	privateKey, _ := base64.StdEncoding.DecodeString(ownPrivateKey)
	privateKey = crypto.PaddingShift(privateKey, 64)
	remotePublicKey, _ := base64.StdEncoding.DecodeString(reqPubkey)

	// Convert the ed25519 private key to a curve25519 private key
	// Curve25519 is only for encryption
	var curve25519SenderPrivateKey [32]byte
	extra25519.PrivateKeyToCurve25519(&curve25519SenderPrivateKey, (*[64]byte)(privateKey))
	var curve25519ReceiverPublicKey [32]byte
	extra25519.PublicKeyToCurve25519(&curve25519ReceiverPublicKey, (*[32]byte)(remotePublicKey))

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

			fmt.Printf("response payload: %v\n", objPayload)
			prettyJSON, _ := json.MarshalIndent(objPayload, "", "  ")
			fmt.Println("Decrypted Message:")
			fmt.Println(string(prettyJSON))

			accountNumber, _ := extractAccountNumber(objPayload)
			fmt.Println("Account Number:")
			fmt.Println(accountNumber)
		}
	}
}

func extractAccountNumber(data interface{}) (string, error) {
	// Assert the top-level interface to a map
	rootMap, ok := data.(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("data is not a map")
	}

	// Navigate to ivms101 map
	ivms101, ok := rootMap["ivms101"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("ivms101 key not found or not a map")
	}

	// Navigate to Beneficiary map
	beneficiary, ok := ivms101["Beneficiary"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("Beneficiary key not found or not a map")
	}

	// Navigate to accountNumber slice
	accountNumbers, ok := beneficiary["accountNumber"].([]interface{})
	if !ok {
		return "", fmt.Errorf("accountNumber key not found or not a slice")
	}

	// Extract the first account number (assuming there's only one)
	if len(accountNumbers) == 0 {
		return "", fmt.Errorf("accountNumber slice is empty")
	}

	accountNumber, ok := accountNumbers[0].(string)
	if !ok {
		return "", fmt.Errorf("accountNumber is not a string")
	}

	return accountNumber, nil
}
