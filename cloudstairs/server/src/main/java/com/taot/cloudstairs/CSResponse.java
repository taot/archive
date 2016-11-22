package com.taot.cloudstairs;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

public class CSResponse {

    @JSONField(name = "Status")
    private String status;

    @JSONField(name = "StatusCode")
    private int statusCode;

    @JSONField(name = "Header")
    private Map<String, List<String>> header;

    @JSONField(name = "Body")
    private String body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "CSResponse [status=" + status + ", statusCode=" + statusCode + ", header=" + header
                + ", body=" + body + "]";
    }
}
