package com.jerry.up.lala.framework.core.data;

import cn.hutool.core.collection.CollectionUtil;
import com.jerry.up.lala.framework.core.common.DataBody;
import com.jerry.up.lala.framework.core.exception.ServiceException;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Description: 校验工具类
 *
 * @author FMJ
 * @date 2023/10/9 15:38
 */
public class CheckUtil {

    public static <T> T dataNull(DataBody<T> dataBody) {
        if (dataBody == null) {
            throw ServiceException.args();
        }
        T value = dataBody.getValue();
        if (value == null) {
            throw ServiceException.args();
        }
        if (value instanceof Collection && CollectionUtil.isEmpty((Collection<?>) value)) {
            throw ServiceException.args();
        }
        if (value instanceof String && StringUtil.isNull((String) value)) {
            throw ServiceException.args();
        }
        return value;
    }

    public static <T, V> void arrayNotContains(T[] array, Function<? super T, ? extends V> mapper, V value) {
        if (!Arrays.stream(array).map(mapper).collect(Collectors.toList()).contains(value)) {
            throw ServiceException.args();
        }
    }
}
