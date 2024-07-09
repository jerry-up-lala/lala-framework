package com.jerry.up.lala.framework.core.request;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.jerry.up.lala.framework.core.common.LoginUser;
import com.jerry.up.lala.framework.core.common.RequestInfo;
import com.jerry.up.lala.framework.core.data.StringUtil;
import com.jerry.up.lala.framework.core.satoken.SaTokenUtil;
import com.jerry.up.lala.framework.core.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: request 解析
 *
 * @author FMJ
 * @date 2023/9/8 17:46
 */
@Slf4j
public class RequestUtil {

    public static RequestInfo requestInfo() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return requestInfo(request);
    }

    public static RequestInfo requestInfo(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String params = MapUtil.join(ServletUtil.getParamMap(request), "&", "=");
        LoginUser loginUser = SaTokenUtil.currentUser();
        StringBuilder userInfo = new StringBuilder();
        String loginName = loginUser.getLoginName();
        String userId = StrUtil.blankToDefault(loginUser.getUserId(), "");
        String tenantId = StrUtil.blankToDefault(TenantContext.getTenantId(), "");
        if (StringUtil.isNotNull(loginName)) {
            userInfo.append("用户名:").append(loginName);
            if (StringUtil.isNotNull(userId)) {
                userInfo.append(",账号ID:").append(userId);
            }
            if (StringUtil.isNotNull(tenantId)) {
                userInfo.append(",集团ID:").append(tenantId);
            }
        }
        return new RequestInfo()
                .setUserAgent(request.getHeader("User-Agent"))
                .setClientIp(ServletUtil.getClientIP(request))
                .setScheme(request.getScheme())
                .setServletPath(request.getServletPath())
                .setServletMethod(request.getMethod())
                .setRequestUrl(request.getRequestURL().toString())
                .setRequestParams(params)
                .setRequestBody(ServletUtil.isMultipart(request) ? null : ServletUtil.getBody(request))
                .setServerName(request.getServerName())
                .setServerPort(request.getServerPort())
                .setRemoteHost(request.getRemoteHost())
                .setRemotePort(request.getRemotePort())
                .setLocalAddr(request.getLocalAddr())
                .setLocalName(request.getLocalName())
                .setLocalPort(request.getLocalPort())
                .setRequestUrlInfo(request.getScheme() + "://" + request.getServerName() +
                        (request.getServerPort() == 80 ? "/lala" : (":" + request.getServerPort())) + request.getServletPath() + (StringUtil.isNull(params) ? "" : "?" + params))
                .setClientInfo(request.getRemoteHost() + "(" + ServletUtil.getClientIP(request) + ":" + request.getRemotePort() + ")")
                .setServerInfo(request.getLocalName() + "(" + request.getLocalAddr() + ":" + request.getLocalPort() + ")")
                .setLoginName(loginName)
                .setUserId(userId)
                .setTenantId(tenantId)
                .setUserInfo(userInfo.toString());
    }

}
