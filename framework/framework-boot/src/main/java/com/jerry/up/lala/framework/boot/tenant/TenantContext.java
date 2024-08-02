package com.jerry.up.lala.framework.boot.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p>Description: 集团上下文内容
 *
 * @author FMJ
 * @date 2023/9/5 10:25
 */
public class TenantContext {

    private static final TransmittableThreadLocal<String> TENANT_ID = new TransmittableThreadLocal<>();

    public static String getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static void clear() {
        TENANT_ID.remove();
    }

}
