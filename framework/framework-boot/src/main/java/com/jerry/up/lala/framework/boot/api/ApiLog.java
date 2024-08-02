package com.jerry.up.lala.framework.boot.api;

import com.jerry.up.lala.framework.common.model.RequestInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>Description: 请求信息
 *
 * @author FMJ
 * @date 2023/9/8 17:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiLog extends RequestInfo {

    /**
     * 模块
     */
    private String apiName;

    /**
     * 类方法
     */
    private String classMethod;

    /**
     * 请求参数
     */
    private String classParams;

    /**
     * 是否成功
     */
    private Boolean responseSuccess;

    /**
     * 响应异常码
     */
    private String responseErrorCode;

    /**
     * 响应异常信息
     */
    private String responseErrorMsg;

    /**
     * 响应耗时(毫秒)
     */
    private Long responseTime;

    /**
     * 响应耗时格式化
     */
    private String responseTimeFormat;

    /**
     * 操作时间
     */
    private Date requestTime;

}
