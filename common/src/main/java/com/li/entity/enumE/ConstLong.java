package com.li.entity.enumE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstLong {
    LOGIN_CODE_TTL(120L);

    // 枚举的字段
    private Long value;
}
