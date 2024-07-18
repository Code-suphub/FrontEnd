package com.li.entity.enumE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstString {
    USER_EXIST(""),
    USER_NAME_NOT_EXIST("用户名不存在"),
    USER_PASSWORD_NOT_MATCH("用户密码错误"),
    USER_NOT_ADMIN("当前用户没有管理员权限"),
    ADMIN("admin"),
    LOGIN_USER_KEY("user_"),
    LOGIN_CODE_KEY("login_code_");

    // 枚举的字段
    private final String value;
}
