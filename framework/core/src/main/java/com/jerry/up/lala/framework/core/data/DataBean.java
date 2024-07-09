package com.jerry.up.lala.framework.core.data;


import java.lang.annotation.*;

/**
 * <p>Description: bean 标识
 *
 * @author FMJ
 * @date 2023/10/25 13:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataBean {

    Class[] targetTypes();

}
