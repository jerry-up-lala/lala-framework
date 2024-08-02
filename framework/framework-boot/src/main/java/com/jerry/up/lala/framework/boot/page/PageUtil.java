package com.jerry.up.lala.framework.boot.page;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import com.jerry.up.lala.framework.common.model.PageQuery;
import com.jerry.up.lala.framework.common.r.PageR;
import com.jerry.up.lala.framework.common.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 分页工具类
 *
 * @author FMJ
 * @date 2023/8/16 17:53
 */
public class PageUtil {

    public static <T> Page<T> initPage(PageQuery pageQuery) {
        if (pageQuery == null || pageQuery.getCurrent() == null || pageQuery.getPageSize() == null) {
            throw ServiceException.args();
        }
        return new Page<>(pageQuery.getCurrent(), pageQuery.getPageSize());
    }

    public static <T, R> Page<R> loadPage(IPage<T> page, List<R> records) {
        return new Page<R>(page.getCurrent(), page.getSize(), page.getTotal()).setRecords(records);
    }

    public static <T> PageR<T> toPageR(IPage<T> page) {
        if (page != null) {
            return new PageR<>(page.getTotal(), page.getRecords());
        }
        return emptyPage();
    }

    public static <T, R> PageR<R> toPageR(IPage<T> page, Class<R> clazz) {
        if (page == null) {
            return emptyPage();
        }
        List<T> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return emptyPage();
        }
        return new PageR<>(page.getTotal(), BeanUtil.toBeanList(records, clazz));
    }

    public static <T, R> PageR<R> toPageR(PageR<T> pageR, Class<R> clazz) {
        if (pageR == null) {
            return emptyPage();
        }
        List<T> list = pageR.getList();
        if (CollUtil.isEmpty(list)) {
            return emptyPage();
        }
        return new PageR<>(pageR.getTotal(), BeanUtil.toBeanList(list, clazz));
    }

    public static <T> PageR<T> emptyPage() {
        return new PageR<>(0L, new ArrayList<>());
    }

}
