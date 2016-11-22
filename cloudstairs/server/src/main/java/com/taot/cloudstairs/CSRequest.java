package com.taot.cloudstairs;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

public class CSRequest {

    @JSONField(name = "Method")
    private String method;

    @JSONField(name = "Header")
    private Map<String, List<String>> header;

    @JSONField(name = "Host")
    private String host;

    @JSONField(name = "PostForm")
    private Map<String, List<String>> postForm;

    @JSONField(name = "RemoteAddr")
    private String remoteAddr;

    @JSONField(name = "RequestURI")
    private String requestURI;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, List<String>> getPostForm() {
        return postForm;
    }

    public void setPostForm(Map<String, List<String>> postForm) {
        this.postForm = postForm;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String toString() {
        return "CSRequest [method=" + method + ", header=" + header + ", host=" + host
                + ", postForm=" + postForm + ", remoteAddr=" + remoteAddr + ", requestURI="
                + requestURI + "]";
    }
}
