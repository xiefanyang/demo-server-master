package com.hnyr.sys.minio.impl;

import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.minio.MinioFileClientService;
import com.hnyr.sys.minio.config.FileResult;
import com.hnyr.sys.minio.config.MinioFileClientSetting;
import com.hnyr.sys.minio.config.PolicyObject;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Component
public class MinioFileClientServiceImpl implements MinioFileClientService {

    private MinioFileClientSetting clientSettings;

    private MinioClient minioClient;

    public MinioFileClientServiceImpl(MinioFileClientSetting clientSettings) {
        this.clientSettings = clientSettings;
    }

    public MinioClient getClient() {
        if (minioClient == null) {
            minioClient = MinioClient.builder().endpoint(clientSettings.getEndpoint()).credentials(clientSettings.getAccessKeyId(), clientSettings.getAccessKeySecret()).build();
        }
        return minioClient;
    }

    private String split = "/";

    @Override
    public FileResult saveFile(byte[] data, String key, Boolean publicRead) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("filename不能为空");
        } else {

            if (key.startsWith(split)) {
                key = key.substring(1, key.length());
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            ObjectWriteResponse result = this.getClient().putObject(PutObjectArgs.builder()
                    .bucket(this.clientSettings.getBucket(publicRead))
                    .object(key)
//                            .contentType(ViewContentType.getContentType(key))
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            FileResult fileResult = new FileResult();
            fileResult.setBucket(this.clientSettings.getBucket(publicRead));
            fileResult.setEndpoint(this.clientSettings.getEndpoint());
            fileResult.setEtag(result.etag());
            fileResult.setKey(key);
            fileResult.setUrl(this.getUrl(publicRead, key, this.clientSettings.getExpiredSeconds()));
            return fileResult;
        }
    }

    @Override
    public FileResult saveFile(InputStream data, String key, long length, Boolean publicRead) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("filename不能为空");
        } else {
            if (key.startsWith(split)) {
                key = key.substring(1, key.length());
            }

            ObjectWriteResponse result = this.getClient().putObject(PutObjectArgs.builder()
                    .bucket(this.clientSettings.getBucket(publicRead))
                    .object(key)
//                            .contentType(ViewContentType.getContentType(key))
                    .stream(data, data.available(), -1)
                    .build());
            FileResult fileResult = new FileResult();
            fileResult.setBucket(this.clientSettings.getBucket(publicRead));
            fileResult.setEndpoint(this.clientSettings.getEndpoint());
            fileResult.setEtag(result.etag());
            fileResult.setKey(key);
            fileResult.setUrl(this.getUrl(publicRead, key, this.clientSettings.getExpiredSeconds()));
            return fileResult;
        }
    }

    @Override
    public FileResult saveFile(File file, String key, Boolean publicRead) throws Exception {
        return this.saveFile(Files.readAllBytes(file.toPath()), key, publicRead);
    }

    @SneakyThrows
    @Override
    public String getUrl(Boolean publicRead, String key, Integer expiredSeconds) {
        if (publicRead) {
            return this.clientSettings.getEndpoint() + split + this.clientSettings.getBucket(publicRead) + split + FilenameUtils.getPath(key) + FilenameUtils.getName(key);
        } else {
            return this.getClient().getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(this.clientSettings.getBucket(publicRead))
                    .object(key)
                    .expiry(expiredSeconds == null ? this.clientSettings.getExpiredSeconds() : expiredSeconds, TimeUnit.SECONDS)
                    .build());
        }
    }

    @Override
    public PolicyObject getPolicyObject() {
        try {
            return null;
        } catch (Exception var12) {
            throw new RuntimeException("获取服务器端签名失败");
        }
    }

    @SneakyThrows
    @Override
    public void deleteFile(Boolean publicRead, String key) {
        this.getClient().removeObject(RemoveObjectArgs.builder()
                .bucket(this.clientSettings.getBucket(publicRead))
                .object(key)
                .build());
    }

    /**
     * 文件的复制
     *
     * @return
     */
    @SneakyThrows
    @Override
    public FileResult copyFile(String sourceKey, String targetKey, Boolean publicRead) {
        try {
            ObjectWriteResponse result = this.minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(this.clientSettings.getBucket(publicRead))
                    .object(targetKey)
                    .source(
                            CopySource.builder()
                                    .bucket(this.clientSettings.getBucket(publicRead))
                                    .object(sourceKey)
                                    .build())
                    .build());
            FileResult fileResult = new FileResult();
            fileResult.setBucket(this.clientSettings.getBucket(publicRead));
            fileResult.setEndpoint(this.clientSettings.getEndpoint());
            fileResult.setEtag(result.etag());
            fileResult.setKey(targetKey);
            fileResult.setUrl(this.getUrl(publicRead, targetKey, this.clientSettings.getExpiredSeconds()));
            return fileResult;
        } catch (Exception e) {
            throw new BusinessException("文件不存在");
        }
    }
}
