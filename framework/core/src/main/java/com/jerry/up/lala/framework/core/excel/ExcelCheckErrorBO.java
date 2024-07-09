package com.jerry.up.lala.framework.core.excel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: Excel 校验错误信息 BO
 *
 * @author FMJ
 * @date 2023/11/15 10:14
 */
@Data
@Accessors(chain = true)
public class ExcelCheckErrorBO {

    /**
     * 表头名称
     */
    private String headerName;

    /**
     * 单元格值
     */
    private String value;

    /**
     * 错误信息
     */
    private String errorMessage;
}
