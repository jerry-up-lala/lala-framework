package com.jerry.up.lala.framework.boot.exception;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.jerry.up.lala.framework.boot.api.ApiLog;
import com.jerry.up.lala.framework.boot.properties.ErrorProperties;
import com.jerry.up.lala.framework.boot.request.RequestUtil;
import com.jerry.up.lala.framework.common.exception.Error;
import com.jerry.up.lala.framework.common.exception.Errors;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import com.jerry.up.lala.framework.common.model.RequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.util.Date;

/**
 * <p>Description: 异常处理
 *
 * @author FMJ
 * @date 2023/9/13 13:55
 */
@Component
@Slf4j
public class ErrorHandle {

    @Autowired
    private ErrorProperties errorProperties;

    @Autowired
    private ExceptionEmailComponent exceptionEmailComponent;

    public ExceptionInfoBO infoBO(Exception e, ApiLog apiLog) {
        return handle(e, apiLog);
    }

    public ExceptionInfoBO infoBO(Exception e) {
        return handle(e, null);
    }

    private ExceptionInfoBO handle(Exception e, ApiLog apiLog) {
        ExceptionInfoBO exceptionInfoBO = loadExceptionInfoBO(e);
        // 错误配置
        if (errorProperties != null) {
            // 捕获异常
            Exception catchException = exceptionInfoBO.getCatchException();
            if (BooleanUtil.isTrue(errorProperties.getCatchPrint()) && catchException != null) {
                log.error("捕获异常", catchException);
            }

            // 业务异常
            ServiceException serviceException = exceptionInfoBO.getServiceException();
            if (BooleanUtil.isTrue(errorProperties.getServicePrint()) && serviceException != null) {
                log.error("业务异常", serviceException);
            }

            // 运行时异常
            if (BooleanUtil.isTrue(errorProperties.getRunTimePrint()) && catchException == null && serviceException == null) {
                log.error("运行时异常", e);
            }

            if (BooleanUtil.isTrue(errorProperties.getMailOpen()) &&
                    errorProperties.getMailLevel() != null && exceptionInfoBO.getExceptionLevel() != null &&
                    errorProperties.getMailLevel() >= exceptionInfoBO.getExceptionLevel()) {
                RequestInfo requestInfo = apiLog == null ? RequestUtil.requestInfo() : apiLog;
                if (requestInfo != null) {
                    ExceptionEmailBO emailExceptionBO = new ExceptionEmailBO()
                            .setServletMethod(requestInfo.getServletMethod())
                            .setRequestUrlInfo(requestInfo.getRequestUrlInfo())
                            .setRequestBody(requestInfo.getRequestBody())
                            .setUserAgent(requestInfo.getUserAgent())
                            .setClientInfo(requestInfo.getClientInfo())
                            .setServerInfo(requestInfo.getServerInfo())
                            .setDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_FORMAT))
                            .setExceptionLevel(exceptionInfoBO.getExceptionLevel())
                            .setErrorCode(exceptionInfoBO.getErrorCode())
                            .setErrorMsg(exceptionInfoBO.getErrorMsg())
                            .setCatchStackTrace(catchException != null ? ExceptionUtil.stacktraceToString(catchException, -1) : "")
                            .setServiceStackTrace(serviceException != null ? ExceptionUtil.stacktraceToString(serviceException, -1) : "")
                            .setRunTimeStackTrace((catchException == null && serviceException == null) ? ExceptionUtil.stacktraceToString(e, -1) : "");
                    exceptionEmailComponent.send(errorProperties.getMailReceivers(), emailExceptionBO);
                }
            }

        }
        return exceptionInfoBO;
    }

    private ExceptionInfoBO loadExceptionInfoBO(Exception e) {
        if (e instanceof UndeclaredThrowableException) {
            e = (Exception) ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }
        if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof ServiceException) {
            e = (ServiceException) e.getCause().getCause();
        }
        if (e instanceof ServiceException) {
            // 处理GlobalException异常
            ServiceException serviceException = (ServiceException) e;
            // 异常信息
            Error error = serviceException.getError();
            // 捕获异常
            Exception catchException = serviceException.getCatchException();
            // 异常级别
            Integer exceptionLevel = serviceException.getExceptionLevel();
            return new ExceptionInfoBO().setError(error).setServiceException(serviceException)
                    .setCatchException(catchException).setExceptionLevel(exceptionLevel);
        }
        // 异常信息
        Error error = loadFrameErrorCode(e);
        return new ExceptionInfoBO().setError(error);
    }

    private Error loadFrameErrorCode(Exception e) {
        // 服务不可用
        if (e instanceof ConnectException) {
            return Errors.SERVER_ERROR;
        }
        // 请求参数不符
        if (e instanceof org.springframework.boot.context.properties.bind.BindException || e instanceof org.springframework.validation.BindException
                || e instanceof HttpMessageNotReadableException || e instanceof HttpMediaTypeException
                || e instanceof MethodArgumentTypeMismatchException || e instanceof MissingServletRequestParameterException) {
            return Errors.ARGS_ERROR;
        }
        // 上传超过文件大小限制
        if (e instanceof MaxUploadSizeExceededException) {
            return Errors.MAX_UPLOAD_SIZE_ERROR;
        }
        // 请求方法错误或者url不存在 直接返回 null
        if (e instanceof HttpRequestMethodNotSupportedException || e instanceof NoHandlerFoundException) {
            return null;
        }
        return Errors.SYSTEM_ERROR;
    }

}
