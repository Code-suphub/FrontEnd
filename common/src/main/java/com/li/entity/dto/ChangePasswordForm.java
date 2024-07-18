package com.li.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForm {
    String oldPassword;
    String newPassword;
    Integer userId;
    String token;
}
