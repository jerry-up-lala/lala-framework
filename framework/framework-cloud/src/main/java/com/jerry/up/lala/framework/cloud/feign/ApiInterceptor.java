package com.jerry.up.lala.framework.cloud.feign;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.util.StrUtil;
import com.jerry.up.lala.framework.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: feign请求 拦截器
 *
 * @author FMJ
 * @date 2024/7/29 13:54
 */
@Component
public class ApiInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String tokenName = SaManager.getConfig().getTokenName();
            String token = attributes.getRequest().getHeader(tokenName);
            if (token != null) {
                requestTemplate.header(tokenName, token);
            }
            HttpServletRequest request = attributes.getRequest();
            String tenantId = request.getHeader(CommonConstant.HEADER_TENANT_ID);
            if (StrUtil.isNotBlank(tenantId)) {
                requestTemplate.header(CommonConstant.HEADER_TENANT_ID, tenantId);
            }
        }
    }
}