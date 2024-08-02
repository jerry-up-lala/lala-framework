package com.jerry.up.lala.framework.boot.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: 集团配置类
 *
 * @author FMJ
 * @date 2023/9/5 09:51
 */
@Data
@Component
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    public static final String PREFIX = "tenant";

    /**
     * 集团模式
     */
    private TenantModeEnum mode = TenantModeEnum.NONE;

    /**
     * 集团字段名
     * 字段模式 必须配置
     */
    private String columnName;

    /**
     * 忽略表 【正则匹配】
     */
    private List<String> ignoreTables;

}
