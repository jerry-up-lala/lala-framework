package com.jerry.up.lala.framework.boot.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>Description: 全局拦截器
 *
 * @author FMJ
 * @date 2023/9/6 10:33
 */
@Configuration
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "mode", havingValue = "DATA_SOURCE")
public class TenantDataSourceInterceptor implements WebMvcConfigurer {

    @Autowired
    private TenantDataSourceHandler tenantDataSourceHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantDataSourceHandler).addPathPatterns("/**");
    }

}
