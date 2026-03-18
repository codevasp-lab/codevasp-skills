package crypto

import (
	"bytes"
	"crypto/rand"
	"encoding/base64"
	"fmt"
	"golang.org/x/crypto/ed25519"
	math "math/rand"
	"time"

	"golang.org/x/crypto/nacl/box"
)

type Curve25519 struct {
	PrivateKey *[32]byte
	PublicKey  *[32]byte
}

func PaddingShift(bb []byte, size int) []byte {
	l := len(bb)
	if l == size {
		return bb
	}
	if l > size {
		return bb[l-size:]
	}
	tmp := make([]byte, size)
	copy(tmp[:size-l], bb)
	return tmp
}

func GetPublicKey(base64OwnPrivateKey string) (string, error) {
	// Decode the base64 private key
	privateKeyBytes, err := base64.StdEncoding.DecodeString(base64OwnPrivateKey)
	if err != nil {
		return "", fmt.Errorf("failed to decode private key: %v", err)
	}

	// Ensure the private key is 32 bytes (seed)
	if len(privateKeyBytes) != ed25519.SeedSize {
		return "", fmt.Errorf("invalid private key length: got %d, want %d", len(privateKeyBytes), ed25519.SeedSize)
	}

	// Generate the full private key from the seed
	privateKey := ed25519.NewKeyFromSeed(privateKeyBytes)

	// Get the public key
	publicKey := privateKey.Public().(ed25519.PublicKey)

	// Encode the public key to base64
	base64PublicKey := base64.StdEncoding.EncodeToString(publicKey)

	return base64PublicKey, nil
}

func Sodium(base64PrivateKey string, base64PublicKey string) (*Curve25519, error) {
	privBytes, _ := base64.StdEncoding.DecodeString(base64PrivateKey)
	pubBytes, _ := base64.StdEncoding.DecodeString(base64PublicKey)

	var privateKey [32]byte
	var publicKey [32]byte

	copy(privateKey[:], privBytes)
	copy(publicKey[:], pubBytes)

	return &Curve25519{PrivateKey: &privateKey, PublicKey: &publicKey}, nil
}

func (cc *Curve25519) Encrypt(msg string) ([]byte, error) {
	var nonce [24]byte
	_, err := rand.Read(nonce[:])
	if err != nil {
		return nil, err
	}

	encrypted := box.Seal(nonce[:], []byte(msg), &nonce, cc.PublicKey, cc.PrivateKey)
	return encrypted, nil
}

func (cc *Curve25519) Decrypt(encrypted []byte) ([]byte, error) {
	var Nonce [24]byte
	copy(Nonce[:], encrypted[:24])

	decrypted, success := box.Open(nil, encrypted[24:], &Nonce, cc.PublicKey, cc.PrivateKey)
	if !success {
		return nil, fmt.Errorf("Failed to decrypt message")
	}

	return decrypted, nil
}

type SignatureContainer struct {
	Nonce     int
	Signature string
	DateTime  string
}

func GenerateSignature(body string, ownPrivateKey string) (SignatureContainer, error) {

	r := math.New(math.NewSource(time.Now().UnixNano()))
	iNonce := r.Intn(10000)

	now := time.Now().UTC()
	strDateTime := now.Format("2006-01-02T15:04:05-0700")

	buffer := new(bytes.Buffer)
	buffer.Write([]byte(strDateTime))
	buffer.Write([]byte(body))
	buffer.Write(ToBytes(iNonce))

	// Decode the base64 private key
	privateKeyBytes, err := base64.StdEncoding.DecodeString(ownPrivateKey)
	if err != nil {
		panic(err)
	}

	var privateKey ed25519.PrivateKey
	var _ ed25519.PublicKey

	switch len(privateKeyBytes) {
	case 32:
		// If it's 32 bytes, it might be a seed for Ed25519
		privateKey = ed25519.NewKeyFromSeed(privateKeyBytes)
		_ = privateKey.Public().(ed25519.PublicKey)
	case 64:
		// If it's 64 bytes, it's likely a full Ed25519 private key
		privateKey = privateKeyBytes
		_ = privateKey.Public().(ed25519.PublicKey)
	default:
		panic(fmt.Sprintf("Unexpected private key length: %d bytes", len(privateKeyBytes)))
	}

	// Sign the message
	signature := ed25519.Sign(privateKey, buffer.Bytes())
	signatureBase64 := base64.StdEncoding.EncodeToString(signature)

	return SignatureContainer{
		Nonce:     iNonce,
		Signature: signatureBase64,
		DateTime:  strDateTime,
	}, nil
}

func ToBytes(i int) []byte {
	result := make([]byte, 4)

	result[0] = byte(i >> 24)
	result[1] = byte(i >> 16)
	result[2] = byte(i >> 8)
	result[3] = byte(i)

	return result
}
