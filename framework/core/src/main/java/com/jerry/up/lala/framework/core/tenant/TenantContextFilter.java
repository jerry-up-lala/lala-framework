package com.jerry.up.lala.framework.core.tenant;

import com.jerry.up.lala.framework.core.common.CommonConstant;
import com.jerry.up.lala.framework.core.common.LoginUser;
import com.jerry.up.lala.framework.core.satoken.SaTokenUtil;
import com.jerry.up.lala.framework.core.data.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Description: 租户内容
 *
 * @author FMJ
 * @date 2023/9/5 10:25
 */
@Slf4j
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = SaTokenUtil.currentUser();
        // 租户ID
        String tenantId = loginUser.getTenantId();
        if (StringUtil.isNull(tenantId)) {
            tenantId = request.getHeader(CommonConstant.HEADER_TENANT_ID);
        }
        TenantContext.setTenantId(tenantId);
        if (tenantId != null) {
            TenantContext.setTenantId(tenantId);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            // 清理
            TenantContext.clear();
        }
    }

}