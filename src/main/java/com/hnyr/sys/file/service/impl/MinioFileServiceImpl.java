package com.hnyr.sys.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.hnyr.sys.file.dao.FileDao;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.entity.po.File;
import com.hnyr.sys.file.entity.vo.FileVo;
import com.hnyr.sys.file.service.MinioFileService;
import com.hnyr.sys.file.util.FileUtil;
import com.hnyr.sys.minio.MinioFileClientService;
import com.hnyr.sys.minio.config.FileResult;
import com.hnyr.sys.minio.config.MinioConst;
import com.hnyr.sys.utils.AudioUtil;
import com.hnyr.sys.utils.DataConvertor;
import com.hnyr.sys.utils.VideoUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MinioFileServiceImpl implements MinioFileService {

    @Resource
    private FileDao fileDao;
    @Resource
    MinioFileClientService fileClient;
    @Value("${spring.servlet.multipart.location:/home/data/file}")
    private String localPath;

    /**
     * @return float
     * @throws
     * @description 图片压缩比例
     * @param[1] srcSize
     * @time 2023/8/23 16:34
     */
    private static float scale200K(long srcSize) {
        float scale = 0f;
        if (srcSize < 200 * 1000) {
            scale = 1.00f;
        } else if (srcSize < 500 * 1000) {
            scale = 0.87f;
        } else if (srcSize < 700 * 1000) {
            scale = 0.77f;
        } else if (srcSize < 1 * 1000 * 1000) {
            scale = 0.67f;
        } else if (srcSize < 2 * 1000 * 1000) {
            scale = 0.55f;
        } else if (srcSize < 4 * 1000 * 1000) {
            scale = 0.47f;
        } else if (srcSize < 5 * 1000 * 1000) {
            scale = 0.43f;
        } else if (srcSize < 10 * 1000 * 1000) {
            scale = 0.35f;
        } else if (srcSize < 15 * 1000 * 1000) {
            scale = 0.30f;
        } else {
            scale = 0.05f;
        }
        return scale;
    }

    @Override
    public String getFileUrl(String recordId) {
        if (!StringUtils.hasText(recordId)) {
            return "";
        }
        FileDto fileDto = getFile(recordId);
        if (fileDto == null) {
            return "";
        }
        String filePath = "/" + fileDto.getCategory() + "/" + fileDto.getOriginalName();
        return fileClient.getUrl(fileDto.getPublicRead(), filePath, null);
    }

    @Override
    public String getFileUrl(String recordId, Integer expiredSeconds) {
        if (!StringUtils.hasText(recordId)) {
            return "";
        }
        FileDto fileDto = getFile(recordId);
        if (fileDto == null) {
            return "";
        }
        String filePath = "/" + fileDto.getCategory() + "/" + fileDto.getOriginalName();
        return fileClient.getUrl(fileDto.getPublicRead(), filePath, expiredSeconds);
    }

    @Override
    public String getFileUrl(Boolean publicRead, String path) {
        return fileClient.getUrl(publicRead, path, null);
    }

    @Override
    public FileDto add(FileDto file) {
        File po = fileDao.add(file);
        BeanUtils.copyProperties(po, file);
        return file;
    }

    @Override
    public FileDto upload(String appCode, Long userId, MultipartFile file, Boolean publicRead, String category, Boolean scale) {
        String url = uploadFile(appCode, userId, file, publicRead, category, scale);
        FileDto fileDto = new FileDto();
        fileDto.setPublicRead(publicRead);
        fileDto.setCategory(category);
        fileDto.setRecordId(IdUtil.fastSimpleUUID());
        fileDto.setFileUrl(url);
        fileDto.setCreateUser(userId);
        fileDto.setOriginalName(file.getOriginalFilename());
        fileDto.setCreateTime(System.currentTimeMillis());
        fileDto.setType(FilenameUtils.getExtension(file.getOriginalFilename()));
        fileDto.setSize(file.getSize());
        fileDto.setSizeStr(FileUtil.changeSize(file.getSize()));
        if (Arrays.stream(MinioConst.VIDEO_TYPE).filter(e -> e.equals(fileDto.getType())).findAny().isPresent()) {
            fileDto.setFileTime(processFileTime(file, fileDto.getType()));
        }

        File po = fileDao.add(fileDto);
        BeanUtil.copyProperties(po, fileDto);
        return fileDto;
    }


    private long processFileTime(MultipartFile multipartFile, String type) {
        long result = 0;
        try {
            java.io.File file = java.io.File.createTempFile(IdUtil.fastSimpleUUID(), "." + type);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            if ("wav".equals(type)) {
                result = AudioUtil.getDuration(file.getAbsolutePath()).longValue();
            } else if ("mp3".equals(type)) {
                result = AudioUtil.getMp3Duration(file).longValue();
            } else if ("m4a".equals(type)) {
                result = VideoUtil.getMp4Duration(file.getAbsolutePath());
            } else if ("mov".equals(type)) {
                result = VideoUtil.getMp4Duration(file.getAbsolutePath());
            } else if ("mp4".equals(type)) {
                result = VideoUtil.getMp4Duration(file.getAbsolutePath());
            }
            file.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    @Override
    public FileDto uploadByte(String appCode, Long userId, byte[] bytes, Boolean publicRead, String category, String fileName) {
        String url = uploadFileByte(appCode, userId, bytes, publicRead, category, fileName);
        FileDto fileDto = new FileDto();
        fileDto.setPublicRead(publicRead);
        fileDto.setCategory(category);
        fileDto.setRecordId(IdUtil.fastSimpleUUID());
        fileDto.setFileUrl(url);
        fileDto.setCreateUser(userId);
        fileDto.setOriginalName(fileName);
        fileDto.setCreateTime(System.currentTimeMillis());
        fileDto.setType(fileName.substring(fileName.indexOf(".")));
        Integer fileSize = bytes.length;
        fileDto.setSize(fileSize.longValue());
        fileDto.setSizeStr(FileUtil.changeSize(fileSize));
        File po = fileDao.add(fileDto);
        BeanUtil.copyProperties(po, fileDto);
        return fileDto;
    }

    private String uploadFileByte(String appCode, Long userId, byte[] bytes, Boolean publicRead, String category, String fileName) {

        String filePath = "/" + appCode + "/" + category + "/" + fileName;
        String url = "";
        try {
            FileResult result = fileClient.saveFile(bytes, filePath, publicRead);
            return result.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @SneakyThrows
    @Override
    public FileDto uploadInputStream(String appCode, Long userId, InputStream inputStream, Boolean publicRead, String category, String fileName) {
        String url = uploadByInputStream(appCode, userId, inputStream, publicRead, category, fileName);
        FileDto fileDto = new FileDto();
        fileDto.setPublicRead(publicRead);
        fileDto.setCategory(category);
        fileDto.setRecordId(IdUtil.fastSimpleUUID());
        fileDto.setFileUrl(url);
        fileDto.setCreateUser(userId);
        fileDto.setOriginalName(fileName);
        fileDto.setCreateTime(System.currentTimeMillis());
        fileDto.setType(fileName.substring(fileName.indexOf(".")));
        Integer fileSize = inputStream.available();
        fileDto.setSize(fileSize.longValue());
        fileDto.setSizeStr(FileUtil.changeSize(fileSize.longValue()));
        File po = fileDao.add(fileDto);
        BeanUtil.copyProperties(po, fileDto);
        return fileDto;
    }

    private String uploadByInputStream(String appCode, Long userId, InputStream inputStream, Boolean publicRead, String category, String fileName) {
        String filePath = "/" + appCode + "/" + category + "/" + fileName;
        String url = "";
        try {
            FileResult result = fileClient.saveFile(inputStream, filePath, 0L, publicRead);
            return result.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public FileDto getFile(String recordId) {
        File po = fileDao.getFile(recordId);
        FileDto fileDto = new FileDto();
        if (po != null) {
            BeanUtils.copyProperties(po, fileDto);
            String filePath =  fileDto.getCategory() + "/" + fileDto.getOriginalName();
            fileDto.setFileUrl(getFileUrl(fileDto.getPublicRead(), filePath));
            return fileDto;
        }
        return null;
    }

    @Override
    public List<FileDto> getFileList(Set<String> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            return null;
        }
        List<File> filePoList = fileDao.findFileList(recordIds);
        List<FileDto> fileDtos = DataConvertor.listConvert(filePoList, FileDto.class);
        fileDtos.forEach(fileDto -> {
            String filePath = fileDto.getCategory() + "/" + fileDto.getOriginalName();
            fileDto.setFileUrl(getFileUrl(fileDto.getPublicRead(), filePath));
        });
        return fileDtos;
    }

    @Override
    public Page<FileDto> page(Pageable pageable, String appCode, String typeName, String categoryId, Boolean enable, Boolean publicRead, String path, String recordId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("appCode", appCode);
        searchMap.put("typeName", typeName);
        searchMap.put("categoryId", categoryId);
        searchMap.put("enable", enable);
        searchMap.put("publicRead", publicRead);
        searchMap.put("path", path);
        searchMap.put("recordId", recordId);
        return DataConvertor.pageConvert(fileDao.page(pageable, searchMap), pageable, FileDto.class);
    }

    @Override
    public void deleteFile(String fileId, Long userId) {
        File po = getById(fileId);
        if (po != null && po.getEnable() == true && po.getDeleted() == false) {
            po.setDeleteUser(userId);
            po.setDeleteTime(System.currentTimeMillis());
            po.setEnable(false);
            fileDao.update(po);
        }
    }

    @Override
    public void clearFile(String fileId, Long userId) {
        File po = getById(fileId);
        if (po != null && po.getEnable() == false && po.getDeleted() == false) {
            po.setCreateUser(userId);
            po.setClearTime(System.currentTimeMillis());
            po.setDeleted(true);
            fileDao.update(po);
            String filePath = "/" + po.getCategory() + "/" + po.getOriginalName();
            fileClient.deleteFile(po.getPublicRead(), filePath);
        }
    }

    @Override
    public Page<FileDto> page(Pageable pageable, Map<String, Object> searchMap) {
        return DataConvertor.pageConvert(fileDao.page(pageable, searchMap), pageable, FileDto.class);
    }

    @Override
    public FileDto getFileResource(String fileId) {
        FileDto fileDto = new FileDto();
        File po = fileDao.getFile(fileId);
        BeanUtil.copyProperties(po, fileDto);
        return fileDto;
    }

    private File getById(String fileId) {
        return fileDao.getFile(fileId);
    }


    private String uploadFile(String appCode, Long userId, MultipartFile file, Boolean publicRead, String category, Boolean scale) {
        String filePath = "/" + category + "/" + file.getOriginalFilename();
        String url = "";
        try {
            String contentType = file.getContentType();
//            if (scale && !StringUtils.isEmpty(contentType) && contentType.startsWith("image") && canYS(contentType)) {
//                String fileName = IdUtil.fastSimpleUUID();
//                java.io.File sourceFile = java.io.File.createTempFile(fileName, "." + FilenameUtils.getExtension(file.getOriginalFilename()));
//                file.transferTo(sourceFile);
//                java.io.File targetFile = java.io.File.createTempFile(fileName + "_temp", "." + FilenameUtils.getExtension(file.getOriginalFilename()));
//                ImgUtil.scale(sourceFile, targetFile, scale200K(file.getSize()));
//                FileResult result = fileClient.saveFile(cn.hutool.core.io.FileUtil.readBytes(targetFile), filePath, publicRead);
//                sourceFile.delete();
//                targetFile.delete();
//                return result.getUrl();
//            } else {
//                FileResult result = fileClient.saveFile(file.getBytes(), filePath, publicRead);
//                return result.getUrl();
//            }
            FileResult result = fileClient.saveFile(file.getBytes(), filePath, publicRead);
            return result.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private Boolean canYs(String contentType) {
        List<String> imgList = new ArrayList<>();
        imgList.add("image/gif");
        imgList.add("image/jpg");
        imgList.add("image/jpeg");
        imgList.add("image/bmp");
        imgList.add("image/png");
        imgList.add("image/psd");
        if (imgList.contains(contentType)) {
            return true;
        }
        return false;
    }


    @Override
    public FileDto copyFile(FileVo sourceVo, String appCode, Long operator, String targetCategory) {
        //复制资源
        File sourcePo = fileDao.getFile(sourceVo.getRecordId());
        String sourceKey = appCode + "/" + sourcePo.getCategory() + "/" + sourcePo.getOriginalName();
        String targetKey = appCode + "/" + targetCategory + "/" + sourcePo.getOriginalName();
        FileResult result = fileClient.copyFile(sourceKey, targetKey, sourcePo.getPublicRead());

        FileDto fileDto = new FileDto();
        fileDto.setPublicRead(sourcePo.getPublicRead());
        fileDto.setCategory(targetCategory);
        fileDto.setRecordId(IdUtil.fastSimpleUUID());
        fileDto.setFileUrl(result.getUrl());
        fileDto.setCreateUser(operator);
        fileDto.setOriginalName(sourcePo.getOriginalName());
        fileDto.setCreateTime(System.currentTimeMillis());
        fileDto.setType(sourcePo.getType());
        fileDto.setSize(sourcePo.getSize());
        fileDto.setFileTime(sourcePo.getFileTime());
        File po = fileDao.add(fileDto);
        BeanUtil.copyProperties(po, fileDto);
        return fileDto;
    }
}
