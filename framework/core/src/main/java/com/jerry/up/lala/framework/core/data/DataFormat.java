package com.jerry.up.lala.framework.core.data;


import java.lang.annotation.*;

/**
 * <p>Description: bean 格式化
 *
 * @author FMJ
 * @date 2023/10/25 13:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFormat {

    /**
     * 原对象 字段名
     */
    String sourceFieldName() default "";

    /**
     * str转为list 分隔符
     */
    String split() default "";

    /**
     * 最大长度
     */
    int maxLength() default -1;

    /**
     * list转为str 索引
     */
    int listIndex() default -1;

    /**
     * 1-date 2-dateTime
     */
    int dateType() default -1;

}
