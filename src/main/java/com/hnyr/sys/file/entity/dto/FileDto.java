package com.hnyr.sys.file.entity.dto;

import com.hnyr.sys.data.entity.BaseBisDto;
import com.hnyr.sys.file.util.FileUtil;
import lombok.Data;

@Data
public class FileDto extends BaseBisDto {

    private String category = "";
    private String type;
    private String suffix;
    private Long size;

    private String originalName;

    private String typeName;
    private Boolean enable;

    private String sizeStr;
    private Long fileTime;

    private Long createUser;
    private Long deleteUser;
    private Long deleteTime;
    private Long clearUser;
    private Long clearTime;

    /**
     * 前台展示的url
     */
    private String fileUrl;
    private String tempUrl;
    private String imgUrl;
    private Boolean publicRead;
    /**
     * 转换文件大小展示
     *
     * @return
     */
    public String getSizeStr() {
        if (this.getSize() == null || this.getSize().longValue() == 0L) {
            return "0B";
        } else {
            return FileUtil.changeSize(this.getSize());
        }
    }

}
