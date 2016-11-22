/*
 * How to use
 *
 * Convert the private key in pkcs1 to pkcs8
 * openssl pkcs8 -topk8 -in key -out key.pkcs8 -nocrypt
 */
package com.taot.cloudstairs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class TestRSA {

    private static String PATH = "/home/taot/tmp/rsa";
    private static String algorithm = "RSA";

    public static void main(String[] args) throws Exception {
        PublicKey pubkey = loadPublicKey();
        System.out.println(pubkey);
//        PrivateKey privkey = loadPrivateKey();
//        System.out.println(privkey);
    }

    private static PublicKey loadPublicKey() throws Exception {
        // Read Public Key.
        File filePublicKey = new File(PATH + "/key.pub.pkcs8");
        String pubkeyStr = readKeyString(filePublicKey);
        System.out.println(pubkeyStr);

        byte[] keyBytes = Base64.decode(pubkeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
//        PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return publicKey;
    }

    private static PrivateKey loadPrivateKey() throws Exception {
        // Read Private Key.
        File filePrivateKey = new File(PATH + "/key.pkcs8");
        String privkeyStr = readKeyString(filePrivateKey);
        System.out.println(privkeyStr);

        byte[] keyBytes = Base64.decode(privkeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(keyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        return privateKey;
    }

    private static String readKeyString(File file) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = bf.readLine();
        while (line != null) {
            if (!line.contains("---")) {
                sb.append(line);
            }
            line = bf.readLine();
        }
        bf.close();
        return sb.toString();
    }

}
