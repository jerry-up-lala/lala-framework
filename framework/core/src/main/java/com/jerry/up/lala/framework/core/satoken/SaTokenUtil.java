package com.jerry.up.lala.framework.core.satoken;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.jerry.up.lala.framework.core.common.Errors;
import com.jerry.up.lala.framework.core.common.LoginUser;
import com.jerry.up.lala.framework.core.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;

/**
 * <p>Description: token 工具类
 *
 * @author FMJ
 * @date 2023/9/4 14:57
 */
@Slf4j
public class SaTokenUtil {

    public static String login(String loginId, LoginUser loginUser) {
        SaLoginModel saLoginModel = SaLoginConfig.create();
        BeanUtil.beanToMap(loginUser).forEach(saLoginModel::setExtra);
        StpUtil.login(loginId, saLoginModel);
        return StpUtil.getTokenValue();
    }

    public static LoginUser currentUser() {
        LoginUser loginUser = new LoginUser();
        if (RequestContextHolder.getRequestAttributes() != null && StpUtil.isLogin()) {
            try {
                SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
                if (saTokenInfo != null && BooleanUtil.isTrue(saTokenInfo.getIsLogin()) && saTokenInfo.getLoginId() != null) {
                    Arrays.stream(ReflectUtil.getFields(LoginUser.class)).forEach(field -> {
                        Object value = StpUtil.getExtra(field.getName());
                        ReflectUtil.setFieldValue(loginUser, field, value);
                    });
                }
            } catch (Exception e) {
                log.warn("获取当前账号异常", e);
            }
        }
        return loginUser;
    }

    public static String loginRError(Throwable e) {
        Errors errors = Errors.LOGIN_NOT_TOKEN;
        if (e instanceof NotLoginException) {
            String type = ((NotLoginException) e).getType();
            if (NotLoginException.INVALID_TOKEN.equals(type)) {
                errors = Errors.LOGIN_INVALID_TOKEN;
            } else if (NotLoginException.TOKEN_TIMEOUT.equals(type)) {
                errors = Errors.LOGIN_TOKEN_TIMEOUT;
            } else if (NotLoginException.BE_REPLACED.equals(type)) {
                errors = Errors.LOGIN_BE_REPLACED;
            } else if (NotLoginException.KICK_OUT.equals(type)) {
                errors = Errors.LOGIN_KICK_OUT;
            }
        }
        return JSONUtil.toJsonStr(R.error(errors));
    }


}
