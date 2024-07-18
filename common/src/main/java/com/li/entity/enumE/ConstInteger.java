package com.li.entity.enumE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstInteger {
    ROLE_ADMIN(1);

    private final Integer value;
}
