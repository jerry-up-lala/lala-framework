package com.jerry.up.lala.framework.core.common;

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

    private Log log;

    private Mail mail;

    private Error error;

    private Upload upload;

    @Data
    public static class Log {

        /**
         * 请求日志传递至消息队列
         */
        private Boolean mq;

        /**
         * 请求日志打印
         */
        private Boolean print;

    }

    @Data
    public static class Mail {

        private String host;

        private Integer port;

        private String from;

        private String user;

        private String pass;

        private String protocol;

        private String subjectPrefix;
    }

    @Data
    public static class Error {

        private Boolean catchPrint;

        private Boolean servicePrint;

        private Boolean runTimePrint;

        private Mail mail;

        @Data
        public static class Mail {

            private Boolean open;

            private Integer level;

            private List<String> receivers;
        }

    }

    @Data
    public static class Upload {

        private Integer max;

        private Integer partition;

    }

}
