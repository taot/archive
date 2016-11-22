package com.taot.cloudstairs.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;


public class RsaUtil {
    
    public static final String RSA = "RSA";
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private RsaUtil() {
    }
    
    public static PublicKey[] loadPubicKeys() {
        InputStream is = RsaUtil.class.getResourceAsStream("/pubkeys.pkcs8");
        if (is == null) {
            throw new RuntimeException("Unable to load public keys from pubkeys.pkcs8");
        }
        List<PublicKey> pubkeys = new ArrayList<PublicKey>();
        
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(is));
            String line = bf.readLine();
            while (line != null) {
                StringBuilder sb = new StringBuilder();
                while (line != null && !line.trim().isEmpty()) {
                    if (!line.contains("---")) {
                        sb.append(line.trim());
                    }
                    line = bf.readLine();
                }
                if (sb.length() > 0) {
                    PublicKey pk = toPublicKey(sb.toString());
                    pubkeys.add(pk);
                }
                line = bf.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public keys. " + e.getMessage(), e);
        }
        
        return pubkeys.toArray(new PublicKey[pubkeys.size()]);
    }
    
    private static PublicKey toPublicKey(String s)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(s);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }
    
    public static byte[] encrypt(PublicKey pk, byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] cipherData = cipher.doFinal(bytes);
            return cipherData;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt with RSA. " + e.getMessage(), e);
        }
    }
}
