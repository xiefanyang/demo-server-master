package com.hnyr.sys.file.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

/**
 * @ClassName: FileUtil
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class FileUtil {

    public static String changeSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        long t = 1024;
        if (size < t) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < t * t) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < t * t * t) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static final String FILE_TYPE_OTHER = "其他";
    public static final String FILE_TYPE_DOC = "文档";
    public static final String FILE_TYPE_VIDEO = "视频";
    public static final String FILE_TYPE_AUDIO = "音频";
    public static final String FILE_TYPE_IMG = "图片";
    public static final String FILE_TYPE_ZIP = "压缩文件";

    public static String getFileTypeName(String suffix) {
        if (FileTypeEnum.AUDIOS.get(suffix) != null) {
            return FILE_TYPE_AUDIO;
        } else if (FileTypeEnum.VIDEOS.get(suffix) != null) {
            return FILE_TYPE_VIDEO;
        } else if (FileTypeEnum.PICS.get(suffix) != null) {
            return FILE_TYPE_IMG;
        } else if (FileTypeEnum.DOCS.get(suffix) != null) {
            return FILE_TYPE_DOC;
        } else if (FileTypeEnum.AUDIOS.get(suffix) != null) {
            return FILE_TYPE_AUDIO;
        } else if (FileTypeEnum.ZIPDOCS.get(suffix) != null) {
            return FILE_TYPE_ZIP;
        } else {
            return FILE_TYPE_OTHER;
        }
    }

    public static String getMimeType(String suffix) {
        if (FileTypeEnum.AUDIOS.get(suffix) != null) {
            return FileTypeEnum.AUDIOS.get(suffix);
        } else if (FileTypeEnum.VIDEOS.get(suffix) != null) {
            return FileTypeEnum.VIDEOS.get(suffix);
        } else if (FileTypeEnum.PICS.get(suffix) != null) {
            return FileTypeEnum.PICS.get(suffix);
        } else if (FileTypeEnum.DOCS.get(suffix) != null) {
            return FileTypeEnum.DOCS.get(suffix);
        } else if (FileTypeEnum.AUDIOS.get(suffix) != null) {
            return FileTypeEnum.AUDIOS.get(suffix);
        } else if (FileTypeEnum.ZIPDOCS.get(suffix) != null) {
            return FileTypeEnum.ZIPDOCS.get(suffix);
        } else {
            return FileTypeEnum.OTHER.get(suffix) == null ? "UNKWON" : FileTypeEnum.OTHER.get(suffix);
        }
    }

    public static String trimToEmpty(Object o) {
        String str = (o == null || "".equals(o)) ? "" : StringUtils.trimToEmpty(String.valueOf(o));
        //转中文逗号避免分隔符干扰
        str = str.replace(",", "，").replace("\r\n", " ").replace("\r", " ").replace("\n", " ");
        return str;
    }
}
