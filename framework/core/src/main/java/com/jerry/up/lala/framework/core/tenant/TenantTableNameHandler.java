package com.jerry.up.lala.framework.core.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

/**
 * <p>Description: 租户处理器 表名隔离
 *
 * @author FMJ
 * @date 2023/9/5 09:17
 */
public class TenantTableNameHandler implements TableNameHandler, TenantHandler {

    private final TenantProperties tenantProperties;

    public TenantTableNameHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Override
    public String dynamicTableName(String sql, String tableName) {
        return tableName + (ignoreTable(tableName, tenantProperties) ? "" : ("_" + TenantContext.getTenantId()));
    }
}
