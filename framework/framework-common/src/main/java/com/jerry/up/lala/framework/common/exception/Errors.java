package com.jerry.up.lala.framework.common.exception;

import com.jerry.up.lala.framework.common.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 全局异常码
 *
 * @author FMJ
 * @date 2023/8/9 17:55
 */
@Getter
@AllArgsConstructor
public enum Errors implements Error {
    /**
     * 全局异常
     */
    SYSTEM_ERROR("100000", "系统异常，请稍后再试"),

    SERVER_ERROR("100001", "系统繁忙，请稍后再试"),

    LOGIN_INVALID_TOKEN_ERROR("100002", "请登录"),

    LOGIN_TIME_OUT_ERROR("100003", "登录状态已过期，请重新登录"),

    NO_ACCESS_ERROR("100004", "无权访问"),

    ARGS_ERROR("100005", "请求参数不符"),

    MAX_UPLOAD_SIZE_ERROR("100006", "上传超过文件大小限制"),

    NOT_FOUND_ERROR("100007", "资源未找到"),

    QUERY_ERROR("100008", "查询失败"),

    SAVE_ERROR("100009", "保存失败"),

    ADD_ERROR("100010", "新增失败"),

    UPDATE_ERROR("100011", "更新失败"),

    DELETE_ERROR("100012", "删除失败"),

    VERIFY_ERROR("100013", "校验失败"),

    DOWNLOAD_ERROR("100014", "下载失败"),

    UPLOAD_ERROR("100015", "上传失败"),

    EXPORT_EMPTY_ERROR("100016", "导出数据不能为空");

    private final String code;

    private final String msg;

    public static Error dynamicMsgError(Error error, String msg) {
        return new Error() {
            @Override
            public String getCode() {
                return error.getCode();
            }

            @Override
            public String getMsg() {
                return StringUtil.isNotNull(msg) ? msg : error.getMsg();
            }
        };
    }
}
