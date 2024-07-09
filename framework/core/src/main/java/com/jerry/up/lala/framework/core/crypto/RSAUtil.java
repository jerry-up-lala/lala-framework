package com.jerry.up.lala.framework.core.crypto;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: rsa 加密解密工具
 *
 * @author FMJ
 * @date 2023/9/5 13:40
 */
@Slf4j
public class RSAUtil {

    public static String encrypt(String decryptStr) {
        try {
            String publicKey = ResourceUtil.readUtf8Str("public.txt");
            // 使用公钥加密
            return SaSecureUtil.rsaEncryptByPublic(publicKey, decryptStr);
        } catch (Exception e) {
            log.error("加密异常", e);
            return "";
        }
    }

    public static String decrypt(String encryptStr) {
        try {
            String privateKey = ResourceUtil.readUtf8Str("private.txt");
            return SaSecureUtil.rsaDecryptByPrivate(privateKey, encryptStr);
        } catch (Exception e) {
            log.error("解密异常", e);
            return "";
        }
    }

}
