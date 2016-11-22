/*
 * How to use:
 *
 * 1. Generate key pair: ssh-keygen -f ./key -m PEM -t rsa
 * 2. Export public key to PKCS8: ssh-keygen -e -f key.pub -m PKCS8 > key.pub.pkcs8
 * 3. Run this
 */

package main

import (
	"encoding/pem"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"fmt"
	"io/ioutil"
	"log"
)

func main() {
	cipher, _ := encode("Hello, world!")
	decode(cipher)
}

func encode(msg string) ([]byte, error) {
	// read public key from file
	keyBytes, err := ioutil.ReadFile("key.pub.pkcs8")
	if err != nil {
		log.Fatal(err)
	}
	block, _ := pem.Decode(keyBytes)

	fmt.Printf("block.Type: %s\n", block.Type)
	pubkeyInterface, err := x509.ParsePKIXPublicKey(block.Bytes)
	if err != nil {
		log.Fatal(err)
	}
	pubkey, ok := pubkeyInterface.(*rsa.PublicKey)
	if !ok {
		log.Fatal("Fatal error")
	}
	fmt.Println(pubkey)
	cipher, err := rsa.EncryptPKCS1v15(rand.Reader, pubkey, []byte(msg))
	if err != nil {
		log.Fatal(err)
	}
	return cipher, nil
}

func decode(cipher []byte) {
	// read private key from file
	keyBytes, err := ioutil.ReadFile("key")
	if err != nil {
		log.Fatal(err)
	}
	block, _ := pem.Decode(keyBytes)
	fmt.Println("block.Type: " + block.Type)
	privkey, err := x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		log.Fatal(err)
	}
	//fmt.Println(privkey)
	out, err := rsa.DecryptPKCS1v15(rand.Reader, privkey, cipher)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(string(out) + "a")
}
