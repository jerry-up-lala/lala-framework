package com.jerry.up.lala.framework.core.common;

/**
 * <p>Description: 异常信息接口
 *
 * @author FMJ
 * @date 2023/8/9 17:49
 */
public interface Error {

    /**
     * 返回错误码
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 返回错误信息
     *
     * @return 错误信息
     */
    String getMsg();
}
