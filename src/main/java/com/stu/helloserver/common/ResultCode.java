package com.stu.helloserver.common;

public enum ResultCode {
    SUCCESS(200, "成功"),
    USER_HAS_EXISTED(409, "用户名已存在"),
    USER_NOT_EXIST(404, "用户不存在"),
    PASSWORD_ERROR(401, "密码错误"),
    REGISTER_FAILED(500, "注册失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}