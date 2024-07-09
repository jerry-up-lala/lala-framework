package com.jerry.up.lala.framework.core.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DataIdBody<ID, VALUE> extends DataBody<VALUE> {

    /**
     * 值
     */
    private ID id;

    public DataIdBody(ID id, VALUE value) {
        super(value);
        this.id = id;
    }

}
