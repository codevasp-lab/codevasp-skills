package main

import (
	"encoding/base64"
	"fmt"
	crypto "github.com/codevasp-lab/go-example/utils"
	"io"
	"net/http"
	"strconv"
	"testing"
	"time"
)

func TestVaspListSearch(t *testing.T) {

	// Convert keys to base64 for easier representation
	base64OwnPrivateKey := "5f6cGTssbOoQ1i41K3OU1WD4EwGPBf9tYS4wJeFSWjE="
	// base64RemotePublicKey := "8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y="

	senderPrivateKey, _ := base64.StdEncoding.DecodeString(base64OwnPrivateKey)
	senderPrivateKey = crypto.PaddingShift(senderPrivateKey, 64)

	// create signature
	signature, err := crypto.GenerateSignature("", base64OwnPrivateKey)

	fmt.Printf("Signature: %s\n", signature.Signature)

	base64OwnPublicKey, err := crypto.GetPublicKey(base64OwnPrivateKey)
	fmt.Println("==========")
	fmt.Println("base64OwnPublicKey:")
	fmt.Println(base64OwnPublicKey)

	// Create the header
	header := http.Header{
		"X-Code-Req-PubKey":    {base64OwnPublicKey},
		"X-Code-Req-Signature": {signature.Signature},
		"X-Request-Origin":     {"code:aasdasda1aa22a2a-aa"},
		"X-Code-Req-Datetime":  {signature.DateTime},
		"X-Code-Req-Nonce":     {strconv.Itoa(signature.Nonce)},
		"Content-Type":         {"application/json"},
	}

	// URL address
	url := fmt.Sprintf("https://trapi-dev.codevasp.com/v1/code/vasps")

	// Create a new request
	req, err := http.NewRequest(http.MethodGet, url, nil)
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
