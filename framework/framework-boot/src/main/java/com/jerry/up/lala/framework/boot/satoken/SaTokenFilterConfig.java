package com.jerry.up.lala.framework.boot.satoken;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jerry.up.lala.framework.boot.properties.CommonProperties;
import com.jerry.up.lala.framework.common.exception.Errors;
import com.jerry.up.lala.framework.common.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 登录拦截
 *
 * @author FMJ
 * @date 2023/9/4 15:53
 */
@Slf4j
@Configuration
public class SaTokenFilterConfig {

    @Autowired
    private CommonProperties commonProperties;

    /**
     * 注册 [sa-token全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setAuth(auth -> SaRouter.match("/**").notMatch(commonProperties.getOpenUrls()).check(StpUtil::checkLogin))
                .setError(e -> {
                    // 无效token 100002-请先登录
                    if (StrUtil.containsAnyIgnoreCase(e.getMessage(), NotLoginException.NOT_TOKEN_MESSAGE,
                            NotLoginException.INVALID_TOKEN_MESSAGE, NotLoginException.DEFAULT_MESSAGE)) {
                        return JSONUtil.toJsonStr(R.error(Errors.LOGIN_INVALID_TOKEN_ERROR));
                    }
                    // token失效 100003-登录状态已过期，请重新登录
                    if (StrUtil.containsAnyIgnoreCase(e.getMessage(), NotLoginException.TOKEN_TIMEOUT_MESSAGE,
                            NotLoginException.BE_REPLACED_MESSAGE, NotLoginException.KICK_OUT_MESSAGE)) {
                        return JSONUtil.toJsonStr(R.error(Errors.LOGIN_TIME_OUT_ERROR));
                    }
                    return JSONUtil.toJsonStr(R.error(Errors.SYSTEM_ERROR));
                });
    }
}
