package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"fmt"
	"io"
	"log"
)

func main() {
	key := []byte("1234567890abcdef")
	plaintext := []byte("example plaintext plaintext is not a multiple of the block ")

	cb := createCipher(key)

	plaintext = padding(plaintext)
	fmt.Printf("plaintext: %s|\n", plaintext)

	// encrypt
	ciphertext := encrypt(cb, plaintext)
	fmt.Printf("encrypted: %x\n", ciphertext)

	// decrypt
	decrypted := decrypt(cb, ciphertext)
	fmt.Printf("decrypted: %s|\n", decrypted)
}

func decrypt(cb cipher.Block, ciphertext []byte) []byte {
	iv := ciphertext[:aes.BlockSize]
	mode := cipher.NewCBCDecrypter(cb, iv)
	plaintext := make([]byte, len(ciphertext) - aes.BlockSize)
	mode.CryptBlocks(plaintext, ciphertext[aes.BlockSize:])
	return plaintext
}

func encrypt(cb cipher.Block, plaintext []byte) []byte {
	if len(plaintext) % aes.BlockSize != 0 {
		panic("plaintext is not a multiple of the block size " + string(aes.BlockSize))
	}
	ciphertext := make([]byte, aes.BlockSize + len(plaintext))
	iv := ciphertext[:aes.BlockSize]
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
		panic(err)
	}

	mode := cipher.NewCBCEncrypter(cb, iv)
	mode.CryptBlocks(ciphertext[aes.BlockSize:], plaintext)
	return ciphertext
}

func createCipher(key []byte) cipher.Block {
	bytes := []byte(key)
	fmt.Println(len(bytes))
	cb, err := aes.NewCipher(bytes)
	if err != nil {
		log.Fatal(err)
	}
	return cb
}

func padding(text []byte) []byte {
	rem := len(text) % aes.BlockSize
	if rem != 0 {
		padded := make([]byte, len(text) + aes.BlockSize - rem)
		for i := 0; i < len(text); i++ {
			padded[i] = text[i]
		}
		for i := 0; i < aes.BlockSize - rem; i++ {
			padded[i + len(text)] = ' '
		}
		return padded
	} else {
		return text
	}
}
