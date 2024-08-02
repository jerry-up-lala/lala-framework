package com.jerry.up.lala.framework.boot.excel;


import java.lang.annotation.*;

/**
 * <p>Description: Excel 格式化
 *
 * @author FMJ
 * @date 2023/10/25 13:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFormat {

    /**
     * 别名
     */
    String headerAlias() default "";

    /**
     * 顺序
     */
    int index() default 0;

    boolean require() default false;

    /**
     * 最小值
     */
    double minValue() default 0d;

    /**
     * 最大值
     */
    double maxValue() default 0d;

    /**
     * 最小长度
     */
    int minLength() default 0;

    /**
     * 最大长度
     */
    int maxLength() default 0;

}
