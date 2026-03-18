package main

import (
	"crypto/rand"
	"encoding/base64"
	"fmt"
	"golang.org/x/crypto/ed25519"
	"testing"

	crypto "github.com/codevasp-lab/go-example/utils"
)

func TestGenerateSignKey(t *testing.T) {
	publicKey, privateKey, _ := ed25519.GenerateKey(rand.Reader)
	// Convert keys to base64
	base64PrivateKey := base64.StdEncoding.EncodeToString(privateKey.Seed()[:])
	base64PublicKey := base64.StdEncoding.EncodeToString(publicKey[:])
	fmt.Println("Private Key (Base64 Encoded): ", base64PrivateKey)
	fmt.Println("Public Key (Base64 Encoded): ", base64PublicKey)

	base64OwnPublicKey, _ := crypto.GetPublicKey(base64PrivateKey)
	fmt.Println("Public Key from Crypto (Base64 Encoded): ", base64OwnPublicKey)
}
