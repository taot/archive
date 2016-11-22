package com.taot.cloudstairs.util;

import java.security.PublicKey;

import org.junit.Assert;
import org.junit.Test;

public class TestRSAUtil {

    @Test
    public void testLoadPublicKeys() {
        PublicKey[] pks = RsaUtil.loadPubicKeys();
        Assert.assertEquals(3, pks.length);
    }
    
    @Test
    public void testEncrypt() {
        PublicKey[] pks = RsaUtil.loadPubicKeys();
        PublicKey pk = pks[0];
        RsaUtil.encrypt(pk, "1234567890abcdef".getBytes());
    }
}
