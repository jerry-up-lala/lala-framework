package com.jerry.up.lala.framework.boot.exception;


import com.jerry.up.lala.framework.common.constant.CommonConstant;
import com.jerry.up.lala.framework.common.exception.Errors;
import com.jerry.up.lala.framework.common.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 自定义异常处理
 *
 * @author FMJ
 * @date 2023/8/9 18:07
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @Autowired
    private ErrorHandle errorHandle;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<R> exceptionHandler(HttpServletRequest req, Exception e) {
        HttpStatus httpStatus = req.getRequestURI().contains(CommonConstant.FEIGN_URL_PREFIX) ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;
        try {
            ExceptionInfoBO exceptionInfoBO = errorHandle.infoBO(e);
            return new ResponseEntity<>(R.error(exceptionInfoBO.getError()), httpStatus);
        } catch (Exception ex) {
            log.error("统一异常处理失败", ex);
            return new ResponseEntity<>(R.error(Errors.SYSTEM_ERROR), httpStatus);
        }
    }


}
