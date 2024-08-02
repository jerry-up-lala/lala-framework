package com.jerry.up.lala.framework.boot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: 通用配置
 *
 * @author FMJ
 * @date 2023/9/6 13:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 数据存储目录
     */
    private String dataPath;

    /**
     * 是否校验权限
     */
    private Boolean access;

    /**
     * 免登录url
     */
    private List<String> openUrls;

}
