package com.hnyr.sys.minio.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@ConfigurationProperties(prefix = "minio.oss")
public class MinioFileClientSetting {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String publicBucket;
    private Integer expiredSeconds = 60;


    public MinioFileClientSetting() {
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket(Boolean publicRead) {
        if (publicRead) {
            Assert.notNull(this.publicBucket, "未配置公共读bucket: aliyun.oss.public-bucket");
            return this.publicBucket;
        } else {
            return this.bucket;
        }
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Integer getExpiredSeconds() {
        return expiredSeconds;
    }

    public void setExpiredSeconds(Integer expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    public void setPublicBucket(String publicBucket) {
        this.publicBucket = publicBucket;
    }
}
