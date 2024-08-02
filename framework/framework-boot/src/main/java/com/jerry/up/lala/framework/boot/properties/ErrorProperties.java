package com.jerry.up.lala.framework.boot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: 异常配置
 *
 * @author FMJ
 * @date 2023/9/6 13:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "error")
public class ErrorProperties {

    private Boolean catchPrint;

    private Boolean servicePrint;

    private Boolean runTimePrint;

    private Boolean mailOpen;

    private Integer mailLevel;

    private List<String> mailReceivers;

}
