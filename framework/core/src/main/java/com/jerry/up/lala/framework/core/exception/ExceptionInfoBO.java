package com.jerry.up.lala.framework.core.exception;

import com.jerry.up.lala.framework.core.common.Error;
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
public class ExceptionInfoBO {

    /**
     * 异常信息
     */
    private Error error;

    /**
     * 手动抛出异常
     */
    private ServiceException serviceException;

    /**
     * 捕获异常
     */
    private Exception catchException;

    /**
     * 异常级别 【级别越低，危险程度越高】
     */
    private Integer exceptionLevel;

    /**
     * 返回错误码
     *
     * @return 错误码
     */
    public String getErrorCode() {
        return error != null ? error.getCode() : null;
    }

    /**
     * 返回错误信息
     *
     * @return 错误信息
     */
    public String getErrorMsg() {
        return error != null ? error.getMsg() : null;
    }


}
