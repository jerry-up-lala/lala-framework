package com.jerry.up.lala.framework.core.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 错误信息
 *
 * @author FMJ
 * @date 2023/8/15 18:02
 */
@Data
@Accessors(chain = true)
public class ErrorR {

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;
}
