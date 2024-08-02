package com.jerry.up.lala.framework.boot.redis;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>Description:查询结果
 *
 * @author FMJ
 * @date 2018/12/21 15:26
 */
@Data
@Accessors(chain = true)
public class RedisLogInfoBO<V> {

    /**
     * 记录日志key
     */
    private String key;

    /**
     * 查询结果
     */
    private List<V> infos;

}