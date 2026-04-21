package com.hnyr.sys.file.util;

/**
 * @ClassName: FileUrl
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class FileUrl {
    public static final String DOWNLOAD_PATH = "/sys/file/download/";
    public static final String IMAGE_PATH = "/sys/file/image/";

    public static String toFileUrl(String fileId) {
        return DOWNLOAD_PATH + fileId;
    }

    public static String toImageUrl(String fileId) {
        return IMAGE_PATH + fileId;
    }

    public static String toFeFileUrl(String fileId) {
        return "downloadFile(\"" + fileId + "\")";
    }

    public static String toFeImageUrl(String fileId) {
        return "loadImage(\"" + fileId + "\")";
    }
}
