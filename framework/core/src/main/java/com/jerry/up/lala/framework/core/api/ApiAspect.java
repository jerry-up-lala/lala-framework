package com.jerry.up.lala.framework.core.api;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.jerry.up.lala.framework.core.common.Error;
import com.jerry.up.lala.framework.core.common.*;
import com.jerry.up.lala.framework.core.exception.ErrorHandle;
import com.jerry.up.lala.framework.core.exception.ExceptionInfoBO;
import com.jerry.up.lala.framework.core.exception.ServiceException;
import com.jerry.up.lala.framework.core.request.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * <p>Description: 请求日志切面
 *
 * @author FMJ
 * @date 2023/9/13 10:27
 */
@Slf4j
@Aspect
@Component
public class ApiAspect {

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private ErrorHandle errorHandle;

    @Autowired
    private RocketMQTemplate rocketMqTemplate;

    @Autowired
    private ApiHandle apiHandle;

    /**
     * 控制层切入点
     */
    @Pointcut("execution(public * com.jerry.up.lala..*.*Ctrl.*(..)) && @annotation(com.jerry.up.lala.framework.core.common.Api)")
    public void pointcut() {
    }

    /**
     * 切入点开始切入内容 如果log_switch 开关开启则入库db 否则只打印日志
     *
     * @param joinPoint 切入点
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 返回结果
        Object result;
        ApiLog apiLog = null;
        CommonProperties.Log logProperties = commonProperties.getLog();
        boolean loadLog = logProperties != null && (BooleanUtil.isTrue(logProperties.getPrint()) || BooleanUtil.isTrue(logProperties.getMq()));
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Api api = AnnotationUtil.getAnnotation(signature.getMethod(), Api.class);
            if (loadLog) {
                apiLog = apiStart(api, signature.getDeclaringTypeName() + "#" + signature.getName(), joinPoint.getArgs());
            }
            if (BooleanUtil.isTrue(commonProperties.getAccess()) && apiHandle.noAccess(api)) {
                throw ServiceException.noAccess();
            }
            result = joinPoint.proceed();
        } catch (Exception e) {
            apiError(apiLog, e);
            throw e;
        } finally {
            try {
                if (loadLog && apiLog != null) {
                    apiEnd(apiLog);
                    if (BooleanUtil.isTrue(logProperties.getPrint())) {
                        log.info("请求信息 {}", JSONUtil.parse(apiLog).toStringPretty());
                    }
                    if (BooleanUtil.isTrue(logProperties.getMq())) {
                        rocketMqTemplate.convertAndSend(CommonConstant.REQUEST_LOG_TOPIC, apiLog);
                    }
                }
            } catch (Exception e) {
                log.error("请求日志发送消息队列异常", e);
            }
        }
        return result;
    }

    private ApiLog apiStart(Api api, String classMethod, Object[] args) {
        if (api != null && BooleanUtil.isFalse(api.log())) {
            return null;
        }
        // 请求开始时间
        Date start = new Date();
        RequestInfo requestInfo = RequestUtil.requestInfo();
        ApiLog apiLog = BeanUtil.toBean(requestInfo, ApiLog.class);
        apiLog.setResponseSuccess(true);
        apiLog.setRequestTime(start);
        apiLog.setClassMethod(classMethod);
        if (ArrayUtil.isNotEmpty(args)) {
            apiLog.setClassParams(Arrays.stream(args)
                    .filter(item -> !(item instanceof ServletResponse || item instanceof ServletRequest || item instanceof MultipartFile))
                    .map(JSONUtil::toJsonStr).collect(Collectors.joining(" ")));
        }
        if (api != null) {
            apiLog.setApiName(api.value());
        }
        return apiLog;
    }

    private void apiError(ApiLog apiLog, Exception e) {
        ExceptionInfoBO exceptionInfoBO = errorHandle.infoBO(e, apiLog);
        Error error = exceptionInfoBO.getError();
        if (apiLog != null) {
            apiLog.setResponseSuccess(false);
            apiLog.setResponseErrorCode(error.getCode());
            apiLog.setResponseErrorMsg(error.getMsg());
        }

    }

    private void apiEnd(ApiLog apiLog) {
        Date start = apiLog.getRequestTime();
        Date end = new Date();
        apiLog.setResponseTime(DateUtil.between(start, end, DateUnit.MS));
        apiLog.setResponseTimeFormat(DateUtil.formatBetween(start, end, BetweenFormatter.Level.MILLISECOND));
    }

}