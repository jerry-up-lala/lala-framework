package com.jerry.up.lala.framework.core.tenant;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.Locale;

/**
 * <p>Description: 租户处理
 *
 * @author FMJ
 * @date 2023/9/5 11:53
 */
public interface TenantHandler {

    default boolean ignoreTable(String tableName, TenantProperties tenantProperties) {
        List<String> ignoreTables = tenantProperties.getIgnoreTables();
        if (CollectionUtil.isEmpty(ignoreTables)) {
            return true;
        }
        return ignoreTables.stream().anyMatch(ignoreTable -> ReUtil.contains(ignoreTable, tableName.toLowerCase(Locale.ROOT)));

    }
}
