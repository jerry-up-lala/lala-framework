package com.jerry.up.lala.framework.core.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 登录账号
 *
 * @author FMJ
 * @date 2023/9/4 14:49
 */
@Data
@Accessors(chain = true)
public class LoginUser {

    /**
     * 账号ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String loginName;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 租户ID
     */
    private String tenantId;

}
