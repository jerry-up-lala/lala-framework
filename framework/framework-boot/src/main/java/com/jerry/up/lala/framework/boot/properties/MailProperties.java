package com.jerry.up.lala.framework.boot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 邮箱配置
 *
 * @author FMJ
 * @date 2023/9/6 13:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String host;

    private Integer port;

    private String from;

    private String user;

    private String pass;

    private String protocol;

    private String subjectPrefix;

}
