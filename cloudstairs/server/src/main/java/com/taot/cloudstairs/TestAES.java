package com.taot.cloudstairs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TestAES {

    public static void main(String[] args) throws Exception {
        String s = "Hello there. How are you? Have a nice day.";

        // Generate key
        // KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // kgen.init(128);
        // SecretKey aesKey = kgen.generateKey();

        SecretKey aesKey = new SecretKeySpec("1234567890abcdef".getBytes(), "AES");

        // Encrypt cipher
        Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);

        // Encrypt
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(
                outputStream, encryptCipher);
        cipherOutputStream.write(s.getBytes());
        cipherOutputStream.flush();
        cipherOutputStream.close();
        byte[] encryptedBytes = outputStream.toByteArray();

        // Decrypt cipher
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
        decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);

        // Decrypt
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
        CipherInputStream cipherInputStream = new CipherInputStream(inStream,
                decryptCipher);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
            outputStream.write(buf, 0, bytesRead);
        }

        System.out.println("Result: " + new String(outputStream.toByteArray()) + "|");

    }
}
