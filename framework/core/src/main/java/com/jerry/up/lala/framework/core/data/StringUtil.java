package com.jerry.up.lala.framework.core.data;

import cn.hutool.core.util.StrUtil;

/**
 * <p>Description: 字符串工具类
 *
 * @author FMJ
 * @date 2023/10/25 13:51
 */
public class StringUtil {

    public static boolean isNotNull(String str) {
        return StrUtil.isNotBlank(str) && !"null".equals(str);
    }

    public static boolean isNull(String str) {
        return !isNotNull(str);
    }

    public static String removeSuffix(String str, String suffix) {
        return str.endsWith(suffix) ? str.substring(0, str.lastIndexOf(suffix)) : str;
    }

    public static String fontRed(String str) {
        return fontRed(str, "");
    }

    public static String fontRed(String str, String suffix) {
        return isNotNull(str) ? suffix + "<font color=\"red\">" + str + "</font>" : "";
    }

}
