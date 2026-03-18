package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	crypto "github.com/codevasp-lab/go-example/utils"
	"io"
	"net/http"
	"strconv"
	"testing"
	"time"
)

func TestReportTransferResult(t *testing.T) {

	// Convert keys to base64 for easier representation
	base64OwnPrivateKey := "5f6cGTssbOoQ1i41K3OU1WD4EwGPBf9tYS4wJeFSWjE="
	base64RemotePublicKey := "8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y="

	senderPrivateKey, _ := base64.StdEncoding.DecodeString(base64OwnPrivateKey)
	senderPrivateKey = crypto.PaddingShift(senderPrivateKey, 64)

	// Create body with encrypted payload
	requestBody := map[string]string{
		"transferId": "d965297b-a0d5-4e26-b007-bcfe55cce051",
		"txid":       "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
		"vout":       "",
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
	url := fmt.Sprintf("https://trapi-dev.codevasp.com/v1/code/transfer/codexchange-non-kor/txid")

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
