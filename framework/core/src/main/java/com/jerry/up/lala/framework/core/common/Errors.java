package com.jerry.up.lala.framework.core.common;

import com.jerry.up.lala.framework.core.data.StringUtil;
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

    LOGIN_NOT_TOKEN("100001", "请登录"),

    LOGIN_INVALID_TOKEN("100001", "请重新登录"),

    LOGIN_TOKEN_TIMEOUT("100001", "会话超时"),

    LOGIN_BE_REPLACED("100001", "已在其他设备登录"),

    LOGIN_KICK_OUT("100001", "已被强制下线"),

    SAME_TOKEN_ERROR("100001", "无权访问"),

    NO_ACCESS_ERROR("100002", "无权访问"),

    SERVER_ERROR("100003", "服务不可用，请稍后重试"),

    ARGS_ERROR("100004", "请求参数不符"),

    MAX_UPLOAD_SIZE_ERROR("100005", "上传超过文件大小限制"),

    NOT_FOUND_ERROR("100006", "资源未找到"),

    QUERY_ERROR("100007", "查询失败"),

    SAVE_ERROR("100008", "保存失败"),

    ADD_ERROR("100009", "新增失败"),

    UPDATE_ERROR("100010", "更新失败"),

    DELETE_ERROR("100011", "删除失败"),

    VERIFY_ERROR("100012", "校验失败"),

    DOWNLOAD_ERROR("100013", "下载失败"),

    UPLOAD_ERROR("100014", "上传失败"),

    EXPORT_EMPTY_ERROR("100015", "导出数据不能为空");

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
