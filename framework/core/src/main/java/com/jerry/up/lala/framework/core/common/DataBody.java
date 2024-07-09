package com.jerry.up.lala.framework.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>Description: id 主键
 *
 * @author FMJ
 * @date 2023/11/3 09:58
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataBody<V> {

    /**
     * 值
     */
    private V value;
}
