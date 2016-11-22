package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/base64"
	//"encoding/hex"
	"encoding/json"
	"encoding/pem"
	//"fmt"
	"io"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	//"runtime/debug"
	"strings"
	"time"
)

var port = "8000"
var climbUrl = "http://localhost:8080/climb"

var getSessionInChann = make(chan int)
var getSessionOutChann = make(chan Session)

func main() {
	go getSession(getSessionInChann, getSessionOutChann)

  http.HandleFunc("/", handler)
  log.Println("Cloudstairs client listening on port " + port)
	err := http.ListenAndServe(":" + port, nil)
	if err != nil {
		log.Println(err)
	}
}

func getSession(in chan int, out chan Session) {
	var session Session
	//var aesCipher cipher.Block = nil
	for {
		command := <- in
		//log.Printf("command: %d, session: %s", command, hex.EncodeToString(session.AesCipherBlock))
		if command != 0 || session.CreateTime == 0 || session.IsExpired() {
			session = requestSessionFromServer()
			// TODO: handle error and timeout
			//log.Printf("Received AES key (HEX): %s\n", hex.EncodeToString(session.AesKey))
			//aesCipher = createCipher(aesKey)
		}
		//log.Printf("AES key (HEX): " + hex.EncodeToString(session.AesKey))
		out <- session
	}
}

func createCipherBlock(key []byte) cipher.Block {
	bytes := []byte(key)
	cb, err := aes.NewCipher(bytes)
	if err != nil {
		log.Fatal(err)
	}
	return cb
}

func requestSessionFromServer() Session {
	resp, err := http.Get("http://localhost:8080/session")
	if err != nil {
		log.Fatal(err)
	}

	// Base64 decode the response
	decoder := base64.NewDecoder(base64.StdEncoding, resp.Body)
	encodedSession, err := ioutil.ReadAll(decoder)

	// RSA decode to get the AES key
	sessionBytes := decodeRSA(encodedSession)
	var csSession CSSession
	log.Println("CSSession json: " + string(sessionBytes))
	err = json.Unmarshal(sessionBytes, &csSession)
	if err != nil {
		log.Fatal(err)
	}
	session := csSession.ToSession()

	return session
}

func decodeRSA(cipher []byte) []byte {
	keyBytes, err := ioutil.ReadFile("privkey")
	if err != nil {
		log.Fatal(err)
	}
	block, _ := pem.Decode(keyBytes)
	privkey, err := x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		log.Fatal(err)
	}
	out, err := rsa.DecryptPKCS1v15(rand.Reader, privkey, cipher)
	if err != nil {
		log.Fatal(err)
	}

	return out
}

func handler(w http.ResponseWriter, r *http.Request) {
	if err := proxy(w, r); err != nil {
		log.Printf("ERROR %s", err)
		http.Error(w, err.Error(), 500)
	}
}

func proxy(w http.ResponseWriter, r *http.Request) error {
	log.Printf("%s %s", r.Method, r.RequestURI)

	// Get AES key
	getSessionInChann <- 0
	session := <- getSessionOutChann
	log.Println(session.AesCipherBlock)

	// Create cloudstairs request and convert to json
	// TODO add encryption
  csr := NewCSRequest(r)
  jsonBytes, err := json.Marshal(csr)
  if err != nil {
		return err
  }
	log.Println("Plain cloudstairs request (JSON): " + string(jsonBytes))

	// encrypt request with AES and base64 encode
	jsonEncrypted := encryptAES(session.AesCipherBlock, jsonBytes)
	jsonEncryptedBase64 := base64.StdEncoding.EncodeToString(jsonEncrypted)

	log.Println("Encrypted cloudstaris request (Base64): " + jsonEncryptedBase64)

	// Send cloudstairs request
  resp, err := http.PostForm(climbUrl, url.Values{"s": { session.Uuid },
		"content": { jsonEncryptedBase64 } })
  if err != nil {
		return err
  }
  defer resp.Body.Close()

	// Read cloudstairs response
	buf, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}

	// Convert cloudstairs response to http response and send it to requester
	var csResp CSResponse
	err = json.Unmarshal(buf, &csResp)
	if err != nil {
		return err
	}
	err = csResp.respond(w)
	if err != nil {
		return err
	}

	return nil
}

func encryptAES(cb cipher.Block, plaintext []byte) []byte {
	if len(plaintext) % aes.BlockSize != 0 {
		plaintext = paddingForAES(plaintext)
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

func paddingForAES(text []byte) []byte {
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

type CSSession struct {
	Timeout int64
	AesKey []byte
	Uuid string
}

func (s CSSession) ToSession() Session {
	var session Session
	session.Timeout = s.Timeout
	session.Uuid = s.Uuid
	session.AesCipherBlock = createCipherBlock(s.AesKey)
	session.CreateTime = time.Now().Unix()
	return session
}

type Session struct {
	Timeout int64
	AesCipherBlock cipher.Block
	Uuid string
	CreateTime int64
}

func (session Session) IsExpired() bool {
	return time.Now().Unix() >= session.CreateTime + session.Timeout
}

type CSRequest struct {
  Method string
  Header map[string][]string
  Host string
  PostForm map[string][]string
  RemoteAddr string
  RequestURI string
}

func NewCSRequest(r *http.Request) *CSRequest {
  csr := new(CSRequest)
  csr.Method = r.Method
  csr.Header = r.Header
  csr.Host = r.Host
  csr.PostForm = r.PostForm
  csr.RemoteAddr = r.RemoteAddr
  csr.RequestURI = r.RequestURI

  return csr
}

type CSResponse struct {
	Status string
	StatusCode int
	Header map[string][]string
	Body string
}

func (resp *CSResponse) respond(w http.ResponseWriter) error {
	header := w.Header()
	for k, v := range resp.Header {
		header[k] = v
	}
	w.WriteHeader(resp.StatusCode)

	strReader := strings.NewReader(resp.Body)
	decoder := base64.NewDecoder(base64.StdEncoding, strReader)

	buf, err := ioutil.ReadAll(decoder)
	_, err = w.Write(buf)

	return err
}
