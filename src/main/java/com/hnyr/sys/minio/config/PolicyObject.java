package com.hnyr.sys.minio.config;

public class PolicyObject {
    private String accessid;
    private String policy;
    private String signature;
    private String expire;
    private String host;
    private String dir;

    public PolicyObject() {
    }

    public String getAccessid() {
        return this.accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getExpire() {
        return this.expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}

