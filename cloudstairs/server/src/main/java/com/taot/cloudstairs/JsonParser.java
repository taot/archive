package com.taot.cloudstairs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class JsonParser {
    
    private static Logger logger = LoggerFactory.getLogger(JsonParser.class);

    private JsonParser() {
    }

    public static CSRequest json2Request(String json) {
        try {
            return JSON.parseObject(json, CSRequest.class);
        } catch (RuntimeException e) {
            logger.error(e.getMessage() + ":" + json);
            throw e;
        }
    }

    public static String response2Json(CSResponse resp) {
        return JSON.toJSONString(resp, false);
    }
    
    public static String session2Json(CSSession session) {
        return JSON.toJSONString(session, false);
    }
}