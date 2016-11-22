package com.taot.cloudstairs.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesUtil {
    
    private static Logger logger = LoggerFactory.getLogger(AesUtil.class);

    private static SecureRandom rand = new SecureRandom();
    
    public static final int BLOCK_SIZE = 16;

    private AesUtil() {
    }
    
    public static byte[] randomAesKey() {
        byte[] bytes = new byte[BLOCK_SIZE];
        synchronized (rand) {
            rand.nextBytes(bytes);
        }
        return bytes;
    }
    
    public static byte[] decrypt(SecretKey key, byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            logger.info("AES Key: " + Hex.encodeHexString(key.getEncoded()));
            
            IvParameterSpec iv = new IvParameterSpec(encryptedData, 0, BLOCK_SIZE);
            
            logger.info("encryptedData.length: " + encryptedData.length);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] data = cipher.doFinal(encryptedData, BLOCK_SIZE, encryptedData.length - BLOCK_SIZE);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting AES. " + e.getMessage(), e);
        }
    }
}
