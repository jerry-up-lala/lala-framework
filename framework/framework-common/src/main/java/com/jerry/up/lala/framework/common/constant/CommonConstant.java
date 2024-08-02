package com.jerry.up.lala.framework.common.constant;

/**
 * <p>Description: 公共常量
 *
 * @author FMJ
 * @date 2023/12/22 10:53
 */
public class CommonConstant {

    /**
     * redis 分隔符
     */
    public static final String REDIS_KEY_SEPARATOR = ":";

    /**
     * redis log
     */
    public static final String REDIS_KEY_LOG = "Redis" + REDIS_KEY_SEPARATOR + "Log" + REDIS_KEY_SEPARATOR;

    /**
     * 日期
     */
    public static final Integer DATE_TYPE_DATE = 1;

    /**
     * 日期时分秒
     */
    public static final Integer DATE_TYPE_TIME = 2;

    /**
     * 请求头集团标识
     */
    public static final String HEADER_TENANT_ID = "TENANT_ID";

    /**
     * 规范服务间调用路由前缀
     */
    public static final String FEIGN_URL_PREFIX = "/feign";
}
