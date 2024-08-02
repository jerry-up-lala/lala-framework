package com.jerry.up.lala.framework.boot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 日志配置
 *
 * @author FMJ
 * @date 2023/9/6 13:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "log")
public class LogProperties {

    /**
     * 请求日志传递至消息队列
     */
    private Boolean mq;

    /**
     * 请求日志主题
     */
    private String mqTopic;

    /**
     * 请求日志打印
     */
    private Boolean print;

}
