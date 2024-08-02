package com.jerry.up.lala.framework.boot.api;

/**
 * <p>Description: 访问控制拦截
 *
 * @author FMJ
 * @date 2023/12/14 10:19
 */
public interface ApiHandle {

    default boolean noAccess(Api api) {
        return false;
    }

}
