package com.jerry.up.lala.framework.boot.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: 接口描述
 *
 * @author FMJ
 * @date 2023/12/14 10:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Api {

    /**
     * 描述
     */
    String value() default "";

    /**
     * 需要校验的权限码
     *
     * @return 需要校验的权限码
     */
    String[] accessCodes() default {};

    /**
     * 是否记录日志
     *
     * @return 默认需要
     */
    boolean log() default true;

}
