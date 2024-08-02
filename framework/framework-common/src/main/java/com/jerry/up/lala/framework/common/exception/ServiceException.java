package com.jerry.up.lala.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>Description: 业务异常
 *
 * @author FMJ
 * @date 2023/8/9 17:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors
public class ServiceException extends RuntimeException {

    /**
     * 异常信息
     */
    private Error error;

    /**
     * 捕获异常
     */
    private Exception catchException;

    /**
     * 异常级别【级别越低越严重】
     */
    private Integer exceptionLevel;

    public ServiceException(Error error, Exception catchException, Integer exceptionLevel) {
        super(error != null ? (error.getCode() + "(" + error.getMsg() + ")") : null);
        this.error = error;
        this.catchException = catchException;
        this.exceptionLevel = exceptionLevel;
    }


    public static ServiceException error() {
        return new ServiceException(Errors.SYSTEM_ERROR, null, 1);
    }

    public static ServiceException args() {
        return new ServiceException(Errors.ARGS_ERROR, null, 1);
    }

    public static ServiceException notFound() {
        return new ServiceException(Errors.NOT_FOUND_ERROR, null, 1);
    }

    public static ServiceException noAccess() {
        return new ServiceException(Errors.NO_ACCESS_ERROR, null, 1);
    }

    public static ServiceException error(Exception catchException) {
        return new ServiceException(Errors.SYSTEM_ERROR, catchException, 1);
    }

    public static ServiceException error(Error error) {
        return new ServiceException(error, null, 1);
    }

    public static ServiceException error(Error error, Exception catchException) {
        return new ServiceException(error, catchException, 1);
    }

    public static ServiceException error(Error error, Integer exceptionLevel) {
        return new ServiceException(error, null, exceptionLevel);
    }

    public static ServiceException error(Error error, Exception catchException, Integer exceptionLevel) {
        return new ServiceException(error, catchException, exceptionLevel);
    }


}
