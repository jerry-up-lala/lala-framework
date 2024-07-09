package com.jerry.up.lala.framework.core.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 请求信息
 *
 * @author FMJ
 * @date 2023/9/8 17:45
 */
@Data
@Accessors(chain = true)
public class RequestInfo {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户名
     */
    private String loginName;

    /**
     * 账号ID
     */
    private String userId;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 请求平台
     */
    private String userAgent;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 请求协议
     */
    private String scheme;

    /**
     * servlet 路径
     */
    private String servletPath;

    /**
     * 请求方式
     */
    private String servletMethod;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求body
     */
    private String requestBody;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 服务端口
     */
    private Integer serverPort;

    /**
     * 客户端host
     */
    private String remoteHost;

    /**
     * 客户端端口
     */
    private Integer remotePort;

    /**
     * 服务端地址
     */
    private String localAddr;

    /**
     * 服务端名称
     */
    private String localName;

    /**
     * 服务端端口
     */
    private Integer localPort;

    /**
     * 请求信息
     */
    private String requestUrlInfo;

    /**
     * 客户端信息
     */
    private String clientInfo;

    /**
     * 服务端信息
     */
    private String serverInfo;

}
