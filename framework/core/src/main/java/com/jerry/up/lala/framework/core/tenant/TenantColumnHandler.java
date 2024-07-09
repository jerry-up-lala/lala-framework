package com.jerry.up.lala.framework.core.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.jerry.up.lala.framework.core.data.StringUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * <p>Description: 租户处理器 字段隔离
 *
 * @author FMJ
 * @date 2023/9/5 09:17
 */
public class TenantColumnHandler implements TenantLineHandler, TenantHandler {

    private final TenantProperties tenantProperties;

    public TenantColumnHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(TenantContext.getTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumnName();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return StringUtil.isNull(TenantContext.getTenantId()) || ignoreTable(tableName, tenantProperties);
    }

}
