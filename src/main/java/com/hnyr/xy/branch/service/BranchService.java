package com.hnyr.xy.branch.service;

import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.service.FileExportContentService;
import com.hnyr.xy.branch.entity.dto.BranchDto;
import com.hnyr.xy.branch.entity.po.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/11 17:42
 * @Version: 1.0
 */
public interface BranchService extends FileExportContentService {
    /*************************************************************
     * 1、本接口包含对文件导入、导出的示例代码，故需继承文件导出基类故有继承FileExportContentService，与前台组件对称；
     * 2、实现类默认在类声明事务，各方法根据需要可设置事务；
     * 3、优先在服务层完成业务的处理、判定、转化等，原则上：dao层只处理数据本身不牵涉业务，controller控制访问权限和参数封装；
     **************************************************************/
    /**
     * 根据rid获取分会信息
     *
     * @param recordId
     * @return
     */
    Branch getByRid(String recordId);

    /**
     * 获取用户管理的分会 rid集合
     *
     * @param userId
     * @return
     */
    List<String> getManagerBranchIds(Long userId);

    /**
     * 保存
     *
     * @param dto
     */
    void save(BranchDto dto);

    /**
     * 分页获取数据
     *
     * @param pageable
     * @param searchMap
     * @return
     */
    Page<BranchDto> page(Pageable pageable, Map<String, Object> searchMap);

    /**
     * 数据文件导入示例
     *
     * @param dto 数据文件导入数据对象
     */
    void importProcess(FileDataProcessRecordDto dto);

    /**
     * 数据导出文件示例
     *
     * @param uid       用户id
     * @param paramBody 数据包
     * @return
     * @throws Exception
     */
    byte[] writeFile(Long uid, Map<String, Object> paramBody) throws Exception;
}
