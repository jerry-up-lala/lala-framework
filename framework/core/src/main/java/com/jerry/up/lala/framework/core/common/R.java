package com.jerry.up.lala.framework.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>Description: 控制层统一返回对象
 *
 * @author FMJ
 * @date 2023/8/9 14:56
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    /**
     * 响应内容
     */
    private T data;

    /**
     * 错误信息
     */
    private ErrorR error;

    public static <T> R<T> succeed(T data) {
        return new R(data, null);
    }

    public static <T> R<T> empty() {
        return new R<>();
    }

    public static R error(Error error) {
        if (error == null) {
            return empty();
        }
        return new R(null, new ErrorR().setCode(error.getCode()).setMsg(error.getMsg()));
    }

}
