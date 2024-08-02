package com.jerry.up.lala.framework.boot.satoken;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jerry.up.lala.framework.common.model.LoginUser;
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

}
