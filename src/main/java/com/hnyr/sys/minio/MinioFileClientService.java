package com.hnyr.sys.minio;

import com.hnyr.sys.minio.config.FileResult;
import com.hnyr.sys.minio.config.PolicyObject;

import java.io.File;
import java.io.InputStream;

public interface MinioFileClientService {

    FileResult saveFile(byte[] var1, String var2, Boolean publicRead) throws Exception;


    FileResult saveFile(InputStream var1, String var2, long var3, Boolean publicRead) throws Exception;

    FileResult saveFile(File var1, String var2, Boolean publicRead) throws Exception;

    String getUrl(Boolean publicRead, String var1, Integer expiredSeconds);

    PolicyObject getPolicyObject();

    void deleteFile(Boolean publicRead, String key);

    FileResult copyFile(String sourceKey, String targetKey, Boolean publicRead);
}
