package com.jerry.up.lala.framework.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>Description: 分页对象
 *
 * @author FMJ
 * @date 2023/8/16 17:45
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageR<T> {

    /**
     * 总条目数
     */
    private Long total;

    /**
     * 结果集
     */
    private List<T> list;


}
