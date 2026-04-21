package com.hnyr.xy.branch.controller;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.service.FileDataProcessRecordService;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.SysUserDataPurviewService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import com.hnyr.xy.branch.entity.dto.BranchDto;
import com.hnyr.xy.branch.service.BranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchController
 * @Description: 校友会controller
 * @Author: demo
 * @CreateDate: 2023/10/11 17:38
 * @Version: 1.0
 */
@Slf4j
@Api("校友系统 - 校友会")
@RestController
@RequestMapping("/api")
public class BranchController {
    @Resource
    BranchService branchService;
    @Resource
    SysUserDataPurviewService sysUserDataPurviewService;
    @Resource
    FileDataProcessRecordService fileDataProcessRecordService;
    /******************************************************************************************************************
     * 本类作为示例代码，相关实际功能需按需求进行完善。
     * 示例对常用前后台交互进行简化编码及说明，可参考编码
     * 说明：
     * 1、当前访问用户信息利用 (类似session），仅限于需要登录状态的api，公开游客api不加该参数
     *  参数：@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu （如需使用当前用户信息，如id，可get使用）
     *  2、数据变更类均使用POST方式，检索类建议使用POST
     *  3、写注释与@ApiOperation
     *  4、@AuditLog ：用户操作行为日志注解，数据变更与重要查询的需要加（如：涉及核心系统数据、隐私数据的查询），非重要查询操作不加。
     *  5、后端权限标签：@AuthPermissions(value = "bis.xy.branch.index") 支持多个 value={"",""} 公开游客不需要添加
     *  6、特殊前台保存特殊字符（如 涉及sql关键字 html标签，导致xss sql注入风险的）会自动拦截，如特殊功能需要提交（如：配置sql语句、富文本提交），
     *     需在application.properties中增加忽略路径：hnyr.exclude.xql=xxx，并严格设置权限@AuthPermissions设置@AuditLog
     *  7、相同数据源，不同数据权限的，应设置不同资源（如：访问全部、访问部分，service和内部区分，但api需要2个），前台资源访问可根据权限大小，设置api路径顺序重写进行匹配
     ******************************************************************************************************************/


    /**
     * 分页获取用户 （分会管理授权，限定数据权限范围）
     *
     * @param tu        访问用户对象
     * @param searchMap 封装传递的检索参数（包含列表分页参数）
     * @return
     */
    @PostMapping("/bis/xy/branch/page")
    @ApiOperation("分页获取校友分会 - 含分会权限过滤")
    @AuthPermissions(value = "bis.xy.branch.index")
    public ResponseResult page(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> searchMap) {

        //TODO @AuthPermissions(value = "bis.xy.branch.index") 本资源设定了数据权限，有2种方式进行数据权限的过滤
        // 【此数据权限判定，正常可在controller层处理，如高频场景或执行时长较长，应适当考虑缓存或传入用户id到service层进行判定，减少非同一事务，多次从连接池获取连接消耗】
        /**
         * 判断数据权限有2种方法，根据实际情况进行处理（二选一即可）
         * （1）统一授权模式：业务中不做设置管理员功能，业务表不标记管理员信息，如：分会成员中的leader字段，如不加该字段，甚至管理员不存在于分会成员中，则为"完全系统管理员角色设定模式"，此模式数据权限托管为角色权限管理模块
         *  该模式：应从"用户授权"中对应角色 添加用户，并为该用户设定可管理的数据权限范围 ，如：添加A用户，选定分会管理权限范围（多选）
         *  好处：减少开发量（不需要定制业务表中的管理员设置功能），管理员隔离出业务避免干扰，根据统一配置，自动获取拼装过滤语句
         *
         * （2）业务授权模式：业务中标记管理员信息，如：分会成员中的leader字段，包含该字段，仅加入角色，数据权限为自管模式【当前功能的方式】
         *  该模式：应自行根据当前用户与要访问的数据自行做数据权限的处理
         *  好处：灵活性好，复杂情况可定制，接口隔离性好
         *
         *  数据权限过滤的后端校验作用：（1）未指定数据访问目标，自动过滤设定权限的数据范围（2）指定数据访问目标，判定是否可进行访问（避免相同功能权限，指向爆破越权访问）
         */
        // TODO （三选一） 方法一 示例： bis.xy.branch.index 该资源为管理员设定了 管理指定的分会，返回结果为 对应的分会id集合，传递到sql里拼装过滤
        List branchIds = sysUserDataPurviewService.getUserDataPurviewIds(tu.getId(), "bis.xy.branch.index");
        // TODO （三选一）  方法二 示例：本业务 因有独立管理员设置功能，此处可先获取管理的分会id集合（recordId）
        // TODO 根据当前用户id（tu.getId())从成员表中 读取 user_id = :userId and enable = 1 and leader = 1 and is_deleted = 0 的所有管理数据，获得管理id
//        List branchIds = branchService.getManagerBranchIds(tu.getId());
        searchMap.put("branchIds", branchIds);
        // TODO （三选一）  方法三（推荐） 示例：本业务有独立管理员设置功能，此处可以传入需要获取权限标志（dao层jdbc联表查询）

        //工具方法解析前台分页参数包装分页对象（注意前后台名称）
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        return ResponseResult.success(branchService.page(pageable, searchMap));
    }

    /**
     * 分页获取用户（总会管理授权，访问所有）
     *
     * @param tu        访问用户对象
     * @param searchMap 封装传递的检索参数（包含列表分页参数）
     * @return
     */
    @PostMapping("/bis/xy/branch/page/all")
    @ApiOperation("分页获取校友分会 - 管理访问全部")
    @AuthPermissions(value = "bis.xy.branch.manager")
    public ResponseResult pageAll(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> searchMap) {
        //工具方法解析前台分页参数包装分页对象（注意前后台名称）
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        return ResponseResult.success(branchService.page(pageable, searchMap));
    }

    /**
     * 保存
     *
     * @param tu  访问用户对象（如需使用当前用户信息，如id，可get使用）
     * @param dto 保存提交的数据包体
     * @return
     */
    @AuditLog
    @PostMapping("/bis/xy/branch/save")
    @ApiOperation("保存校友分会")
    @AuthPermissions(value = "bis.xy.branch.manager")
    public ResponseResult save(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody BranchDto dto) {
//       Long userId =  tu.getId(); 如保存时 需要保存创建人或者最后更新人，可获取后 set入dto
        //TODO 需判定当前用户是否有处理当前用户的权限。参考上面说明，  推荐 传入操作用户id(tu.getId())  在service中进行判定
        branchService.save(dto);
        return ResponseResult.success();
    }

    /****************************** 按模版批量导入数据及处理示例 start ******************************/
    @PostMapping("/bis/xy/branch/import")
    @ApiOperation("校友分会批量导入")
    public ResponseResult importTest(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> copeMap) {
        try {
            String fileId = MapUtil.getStr(copeMap, "fileId");
            String bis = MapUtil.getStr(copeMap, "bis");
            AssertUtil.isTrue(null != fileId, "缺少fileId");
            AssertUtil.isTrue(null != bis, "缺少bis");
            copeMap.put("userName", tu.getUsername());
            FileDataProcessRecordDto processRecordDto = new FileDataProcessRecordDto();
            processRecordDto.setState(0);
            processRecordDto.setCreator(tu.getId());
            processRecordDto.setFileId(fileId);
            processRecordDto.setBis(bis);
            processRecordDto.setServiceName("branchService");
            processRecordDto.setMethodName("importProcess");
            processRecordDto.setParams(copeMap);

            // 集成测试、正式使用走kafka消息队列 异步处理
            fileDataProcessRecordService.addToQueue(processRecordDto);

            // 开发调试可不用kafka  直接处理数据
//            fileDataProcessRecordService.addFileData(processRecordDto);
//            branch.importProcess(fileDataProcessRecordDto);
            return ResponseResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.success(false);
        }
    }
    /****************************** 按模版导入数据及处理示例 end ******************************/
}
