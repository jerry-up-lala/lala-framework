package com.jerry.up.lala.framework.boot.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.Locale;

/**
 * <p>Description: 集团处理
 *
 * @author FMJ
 * @date 2023/9/5 11:53
 */
public interface TenantHandler {

    default boolean ignoreTable(String tableName, TenantProperties tenantProperties) {
        List<String> ignoreTables = tenantProperties.getIgnoreTables();
        if (CollUtil.isEmpty(ignoreTables)) {
            return true;
        }
        return ignoreTables.stream().anyMatch(ignoreTable -> ReUtil.contains(ignoreTable, tableName.toLowerCase(Locale.ROOT)));

    }
}
