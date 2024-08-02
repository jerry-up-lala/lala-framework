package com.jerry.up.lala.framework.cloud.feign;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jerry.up.lala.framework.common.exception.Error;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import com.jerry.up.lala.framework.common.r.R;
import feign.FeignException;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * <p>Description: fallback异常处理
 *
 * @author FMJ
 * @date 2024/7/29 14:51
 */
public class FallbackFactoryUtil {

    public static void throwException(Throwable throwable) throws ServiceException {
        if (throwable instanceof FeignException) {
            Optional<ByteBuffer> optional = ((FeignException) throwable).responseBody();
            if (optional.isPresent()) {
                String content = new String(optional.get().array());
                if (StrUtil.isNotBlank(content)) {
                    R r = JSONUtil.toBean(content, R.class);
                    if (r != null && r.getError() != null) {
                        Error error = new Error() {
                            @Override
                            public String getCode() {
                                return r.getError().getCode();
                            }

                            @Override
                            public String getMsg() {
                                return r.getError().getMsg();
                            }
                        };
                        throw ServiceException.error(error);
                    }
                }
            }
        }
        throw ServiceException.error();
    }
}
