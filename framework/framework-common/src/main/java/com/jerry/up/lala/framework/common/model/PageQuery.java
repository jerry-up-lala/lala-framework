package com.jerry.up.lala.framework.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 分页查询条件
 *
 * @author FMJ
 * @date 2023/8/16 17:45
 */
@Data
@Accessors(chain = true)
public class PageQuery {

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 当前页大小
     */
    private Long pageSize;

}
