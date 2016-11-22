package com.taot.cloudstairs;

import com.alibaba.fastjson.annotation.JSONField;

public class CSSession {

    @JSONField(name = "Timeout")
    private final long timeout;

    @JSONField(name = "AesKey")
    private final byte[] aesKey;
    
    @JSONField(name = "Uuid")
    private final String uuid;
    
    public CSSession(String uuid, byte[] aesKey, long timeout) {
        this.uuid = uuid;
        this.aesKey = aesKey;
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public byte[] getAesKey() {
        return aesKey;
    }

    public String getUuid() {
        return uuid;
    }
}
