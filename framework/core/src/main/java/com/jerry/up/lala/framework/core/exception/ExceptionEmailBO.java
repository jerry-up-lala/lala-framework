package com.jerry.up.lala.framework.core.exception;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 异常信息
 *
 * @author FMJ
 * @date 2023/8/15 17:59
 * @since v1.0.0
 */
@Data
@Accessors(chain = true)
public class ExceptionEmailBO {

    /**
     * 请求方式
     */
    private String servletMethod;

    /**
     * 请求信息
     */
    private String requestUrlInfo;

    /**
     * 请求body
     */
    private String requestBody;

    /**
     * 客户端平台
     */
    private String userAgent;

    /**
     * 客户端信息
     */
    private String clientInfo;

    /**
     * 服务端信息
     */
    private String serverInfo;

    /**
     * 日期
     */
    private String date;

    /**
     * 异常级别
     */
    private Integer exceptionLevel;

    /**
     * 异常code
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 捕获异常
     */
    private String catchStackTrace;

    /**
     * 业务异常
     */
    private String serviceStackTrace;

    /**
     * 运行时异常
     */
    private String runTimeStackTrace;

}
