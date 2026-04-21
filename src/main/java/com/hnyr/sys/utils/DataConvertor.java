package com.hnyr.sys.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DataConvertor
 * @Description: 数据转换工具
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
public class DataConvertor {
    /**
     * 封装pageable对象
     *
     * @param page
     * @param pageSize
     * @param sort     排序字段（+ asc - desc）
     * @return
     */
    public static Pageable pageable(Integer page, Integer pageSize, String sort) {
        Pageable pageable = null;
        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        if (StringUtils.hasText(sort)) {
            pageable = PageRequest.of(page, pageSize,
                    Sort.by(StringUtils.startsWithIgnoreCase(sort, "+") ? Sort.Order.asc(sort.substring(1)) : Sort.Order.desc(sort.substring(1))));
        } else {
            pageable = PageRequest.of(page, pageSize);
        }
        return pageable;
    }

    /**
     * 新版数据分页包装
     *
     * @param page      页码
     * @param pageSize  每页显示数量
     * @param sortField 排序字段
     * @param sortOrder 排序字段
     * @return
     */
    public static Pageable pageable(Integer page, Integer pageSize, String sortField, String sortOrder) {
        Pageable pageable = null;
        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        if (StringUtils.hasText(sortField)) {
            pageable = PageRequest.of(page, pageSize,
                    Sort.by("descend".equals(sortOrder) ? Sort.Order.desc(sortField) : Sort.Order.asc(sortField)));
        } else {
            pageable = PageRequest.of(page, pageSize);
        }
        return pageable;
    }

    /**
     * page对象转换
     *
     * @param sourcePage 数据源对象
     * @param pageable   分页对象
     * @param voClass    要转换的目标对象
     * @param <T>
     * @return
     */
    public static <T> Page<T> pageConvert(Page sourcePage, Pageable pageable, Class voClass) {
        List voList = new ArrayList<>();
        if (sourcePage.getContent() != null && sourcePage.getContent().size() > 0) {
            try {
                for (Object obj : sourcePage.getContent()) {
                    Object o = voClass.newInstance();
                    BeanUtil.copyProperties(obj, o);
                    voList.add(o);
                }
            } catch (Exception e) {
                log.error("数据转换异常:{}", e.getMessage());
                throw new BusinessException("数据转换异常");
            }

        }
        return new PageImpl<T>(voList, pageable, sourcePage.getTotalElements());
    }

    /**
     * list对象转换
     *
     * @param sourceList 数据源对象
     * @param voClass    要转换的目标对象
     * @param <T>
     * @return
     */
    public static <T> List<T> listConvert(List sourceList, Class voClass) {
        List voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sourceList)) {
            try {
                for (Object obj : sourceList) {
                    Object o = voClass.newInstance();
                    BeanUtil.copyProperties(obj, o);
                    voList.add(o);
                }
            } catch (Exception e) {
                log.error("数据转换异常:{}", e.getMessage());
                throw new BusinessException("数据转换异常");
            }

        }
        return voList;
    }

    static String sortField = "sortField";
    static String sortOrder = "sortOrder";

    public static Pageable parseInSearchMap(Map<String, Object> searchMap) {
        Integer page = MapUtil.getInt(searchMap, "current");
        Integer size = MapUtil.getInt(searchMap, "pageSize");
        AssertUtil.isTrue(page != null, "page不能为空");
        AssertUtil.isTrue(size != null, "size不能为空");
        String sortField = null;
        if (StringUtils.hasText(MapUtil.getStr(searchMap, sortField))) {
            sortField = MapUtil.getStr(searchMap, sortField);
        }
        String sortOrder = null;
        if (StringUtils.hasText(MapUtil.getStr(searchMap, sortOrder))) {
            sortOrder = MapUtil.getStr(searchMap, sortOrder);
        }
        return DataConvertor.pageable(page - 1, size, sortField, sortOrder);
    }

    public static <T> Page<T> listToPage(List<T> list, Pageable pageable, int count) {
        return new PageImpl<T>(list, pageable, count);
    }
}
