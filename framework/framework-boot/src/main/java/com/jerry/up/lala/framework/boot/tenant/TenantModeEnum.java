package com.jerry.up.lala.framework.boot.tenant;

import lombok.Getter;

/**
 * <p>Description: 集团模式
 * NONE-非集团模式,COLUMN_NAME-字段模式,TABLE_NAME-表名模式,DATA_SOURCE-数据源模式
 * @author FMJ
 * @date 2023/9/5 09:52
 */
@Getter
public enum TenantModeEnum {

    /**
     * 非集团模式
     */
    NONE("非集团模式"),
    /**
     * 字段模式 在sql中拼接 tenant_id 字段
     */
    COLUMN_NAME("字段模式"),
    /**
     * 表名模式 在sql中表名后缀拼接 _tenant_id
     */
    TABLE_NAME("表名模式"),
    /**
     * 数据源模式 不同集团使用数据源不一致
     */
    DATA_SOURCE("独立数据源模式");

    private final String description;

    TenantModeEnum(String description) {
        this.description = description;
    }
}
