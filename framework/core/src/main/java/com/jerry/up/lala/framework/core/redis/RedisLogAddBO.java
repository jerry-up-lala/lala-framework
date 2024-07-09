package com.jerry.up.lala.framework.core.redis;

import com.jerry.up.lala.framework.core.common.DataBody;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>Description: 记录日志BO
 *
 * @author FMJ
 * @date 2023/11/3 09:47
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RedisLogAddBO<V> extends DataBody<String> {

    /**
     * 内容
     */
    private V info;

}
