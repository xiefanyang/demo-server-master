package com.hnyr.xy.branch.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.service.FileDataProcessRecordService;
import com.hnyr.sys.file.service.MinioFileService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import com.hnyr.sys.utils.ExcelUtil;
import com.hnyr.xy.branch.dao.BranchDao;
import com.hnyr.xy.branch.entity.dto.BranchDto;
import com.hnyr.xy.branch.entity.po.Branch;
import com.hnyr.xy.branch.service.BranchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: BranchServiceImpl
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/11 17:44
 * @Version: 1.0
 */
@Service("branchService")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BranchServiceImpl implements BranchService {
    @Resource
    BranchDao branchDao;

    @Override
    public Branch getByRid(String recordId) {
        return branchDao.getByRid(recordId);
    }

    @Override
    public List<String> getManagerBranchIds(Long userId) {
        return branchDao.getManagerBranchIds(userId);
    }

    @Override
    public void save(BranchDto dto) {
        Branch entity;
        if (StringUtils.isNotBlank(dto.getRecordId())) {
            entity = branchDao.getByRid(dto.getRecordId());
            AssertUtil.notNull(entity, "未找到要更新的数据");
            BeanUtil.copyProperties(dto, entity, "id", "createTime", "version");
        } else {
            entity = BeanUtil.copyProperties(dto, Branch.class, "id", "createTime", "version");
        }
        branchDao.save(entity);
    }

    @Override
    public Page<BranchDto> page(Pageable pageable, Map<String, Object> searchMap) {
//       Page<BranchDto> page = branchDao.pageJdbc(pageable, searchMap);
        Page<BranchDto> page = DataConvertor.pageConvert(branchDao.page(pageable, searchMap), pageable, BranchDto.class);
        if (!CollectionUtils.isEmpty(page.getContent())) {
            //拼装文件对象（logo）
            Set<String> fileIds = new HashSet<>();
            page.getContent().forEach(s -> {
                if (StringUtils.isNotBlank(s.getLogo())) {
                    fileIds.add(s.getLogo());
                }
            });
            List<FileDto> files = minioFileService.getFileList(fileIds);
            if (!CollectionUtils.isEmpty(files)) {
                Map<String, FileDto> fm = new HashMap<>();
                files.forEach(s -> {
                    fm.put(s.getRecordId(), s);
                });
                page.getContent().forEach(s -> {
                    if (StringUtils.isNotBlank(s.getLogo())) {
                        s.setLogoFile(fm.get(s.getLogo()));
                    }
                });
            }
        }
        return page;
    }

    /************************ 以下为导入处理的代码 *******************************
     * 导入的异步处理流程说明：
     * 1、前台导入组件上传文件，上传完成发起调用处理请求；
     * 2、后端controller接收后 对数据进行包装（包括指定service的处理方法名），加入处理队列（存储处理记录），发送队列消息（kafka）
     * 3、消息消费监听动态获取指定service调用指定方法进行处理
     * 4、处理完成，更新处理记录的状态和结果
     **************************************************************************** */
    @Resource
    FileDataProcessRecordService fileDataProcessRecordService;
    @Resource
    MinioFileService minioFileService;

    @Override
    @SneakyThrows
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public void importProcess(FileDataProcessRecordDto dto) {
        try {
            Map<String, Object> otherMap = dto.getParams();
            Integer courseId = MapUtil.getInt(otherMap, "courseId");
            // 开始处理
            dto.setState(1);
            fileDataProcessRecordService.updateFileData(dto);
            String fileId = dto.getFileId();
            FileDto file = minioFileService.getFileResource(fileId);
            URL url = new URL(file.getFileUrl());
            URLConnection uc = url.openConnection();
            // 获取输入流
            InputStream inputStream = url.openStream();

            AssertUtil.isTrue(null != file, "未读取到数据文件");
            dto.setErrors(new ArrayList<>());
            AtomicInteger i = new AtomicInteger(1);
            List<String> userIdNumbers = new ArrayList<>();
            // excel从文件里读数据  要读取的数据对象 需要增加@ExcelProperty注解，如：    @ExcelProperty("工号") 需要与excel一致
            EasyExcel.read(inputStream, BranchDto.class, new PageReadListener<BranchDto>(dataList -> {
                dto.setCountNum(dataList.size());
                dto.setFailNum(0);
                for (BranchDto row : dataList) {
                    log.info("读取到一条数据{}", row);
                    i.getAndIncrement();
                    try {
                        // 单行处理
                        //处理数据 ，参数根据需要传递， ！！！注意：单条数据处理时，应避免重复查询，比如 有公共数据可以一次性读出后转换Map 传入 减少数据查询次数
                        Boolean flag = processOneData(dto, row, null, null);
                        // true 单行处理成功 ； false 跳过 ； 有异常 为失败
                        if (flag) {
                            dto.setSuccessNum(dto.getSuccessNum() + 1);
                        } else {
                            dto.setSkipNum(dto.getSkipNum() + 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        String error = e instanceof BusinessException ? e.getMessage() : "数据处理异常";
                        dto.getErrors().add("行" + (i.get()) + ":" + error);
                        dto.setFailNum(dto.getFailNum() + 1);
                    }
                }
                if (dto.getSkipNum() > 0) {
                    throw new RuntimeException("保存异常数据记录");
                }
            })).sheet().doRead();
            dto.setFinishedTime(System.currentTimeMillis());
            dto.setState(2);
            fileDataProcessRecordService.updateFileData(dto);
            // todo 可以根据需要处理其他
        } catch (RuntimeException e) {
            log.error("{}", e.getMessage());
            dto.setFinishedTime(System.currentTimeMillis());
            dto.setState(3).setResultContent(!CollectionUtils.isEmpty(dto.getErrors()) ? String.join(";", dto.getErrors()) : e.getMessage());
            fileDataProcessRecordService.updateFileData(dto);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            dto.setFinishedTime(System.currentTimeMillis());
            dto.setState(3).setResultContent(!CollectionUtils.isEmpty(dto.getErrors()) ? String.join(";", dto.getErrors()) : "处理数据异常");
            fileDataProcessRecordService.updateFileData(dto);
        }
    }

    /**
     * 处理每行数据（单行事务） 仅为示例
     *
     * @param vo                   导入记录对象（用于导入文件的处理记录）
     * @param dataRow              行数据
     * @param existMajorLocalCodes 根据需要传入预先批量检索的集合 （适用于基本类型）：避免每行检索比较
     * @param departments          根据需要传入预先批量检索的对象集合，包装Map（适用于 对象比较，提升效率）：避免每行检索比较
     * @return true成功 false跳过  错误是抛出异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean processOneData(FileDataProcessRecordDto vo, BranchDto dataRow,
                                  Set<String> existMajorLocalCodes,
                                  Map<String, String> departments) {
        /**
         * 本方法为处理导入的每一行数据，需要进行检查、转化、保存等
         * 做检查时，如需要检索数据库来判断合法性，尽可能在外部批量检索后，传入进行内部检查，避免在本方法内做数据库查询（一行执行一次本方法，性能消耗较大）
         * 处理检查不通过，抛出异常；保存时出现异常，应抛出业务异常
         */
//
//        dataRow.setLocalCode(StringUtils.trimToEmpty(dataRow.getLocalCode()).replace(" ", ""));
//        AssertUtil.isTrue(StringUtils.isNotBlank(dataRow.getLocalCode()), "专业代码（本校）必填");
//        if (existMajorLocalCodes.contains(dataRow.getLocalCode())) {
//            throw new BusinessException("该专业代码（本校）已存在：" + dataRow.getLocalCode());
//        }
//
        return true;
    }

    /**
     * 数据导出的示例
     *
     * @param uid       用户id
     * @param paramBody 数据包
     * @return
     * @throws Exception
     */
    @Override
    public byte[] writeFile(Long uid, Map<String, Object> paramBody) throws Exception {
        // 必须分页处理，避免内存占用过大
        Pageable pageable = PageRequest.of(0, 500);
        // 搜索参数
        Map<String, Object> searchMap = new HashMap<>();
        //TODO 处理用户数据权限

        searchMap.put("enable", true);

        Page<Branch> page = branchDao.page(pageable, searchMap);

        SXSSFWorkbook wb = null;
        String sheetName = "分会信息";
        int totalPage = page.getTotalPages();
        List<String> headTitle = getHeadTitle();
        List<String> headTitleStr = getHeadTitleStr();

        wb = ExcelUtil.writeTitle(wb, sheetName, sheetName, Boolean.TRUE, Boolean.TRUE, 0, 0, 0, headTitle.size() - 1);
        wb = ExcelUtil.writeHead(wb, sheetName, 1, headTitle, Boolean.TRUE);
        int row = 2;
        Map<String, Object> map;
        int xu = 1;
        List<Map<String, Object>> list;
        if (totalPage >= 1) {
            for (int i = 1; i < totalPage + 1; i++) {
                list = new ArrayList<>();
                if (page.getContent() != null && page.getContent().size() > 0) {
                    for (Branch timeMap : page.getContent()) {
                        //处理每行数据
                        map = new HashMap<>();
                        map.put("序号", xu);
                        map.putAll(BeanUtil.beanToMap(timeMap));
                        list.add(map);
                        xu++;
                    }
                }
                ExcelUtil.writeBody(wb, sheetName, row, headTitleStr, list, Boolean.TRUE);
                row = row + list.size();
                pageable = pageable.next();
                page = branchDao.page(pageable, searchMap);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        baos.close();
        return baos.toByteArray();
    }

    private List<String> getHeadTitleStr() {
        List<String> headTitle = new ArrayList<>();
        headTitle.add("序号");
        headTitle.add("name");
        return headTitle;
    }

    private List<String> getHeadTitle() {
        List<String> headTitle = new ArrayList<>();
        headTitle.add("序号");
        headTitle.add("分会名称");
        return headTitle;
    }
}
