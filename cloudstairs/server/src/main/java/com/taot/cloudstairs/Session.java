package com.taot.cloudstairs;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.taot.cloudstairs.util.AesUtil;

public class Session {

    private final String uuid;
    private final long createTime;
    private final SecretKey aesKey;
    
    private final long timeout;
    
    public Session() {
        this.createTime = System.currentTimeMillis();
        this.timeout = 1000 * 60;    // TODO timeout set to 100ms for testing, read it from config later
        this.aesKey = new SecretKeySpec(AesUtil.randomAesKey(), "AES");
        this.uuid = UUID.randomUUID().toString();
    }
    
    public CSSession toCSSession() {
        return new CSSession(uuid, aesKey.getEncoded(), timeout);
    }

    public long getTimeout() {
        return timeout;
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public String getUuid() {
        return uuid;
    }
    
    public boolean isExpired() {
        return createTime + timeout <= System.currentTimeMillis();
    }
    
    public String toString() {
        return "Session(uuid=" + uuid + ", aesKey=" + Hex.encodeHexString(aesKey.getEncoded()) +
                ", createTime=" + (new Date(createTime)).toString() + ", 5timeout=" + timeout + ")";
    }

}
