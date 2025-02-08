package com.StudyCafe_R.modules.account.dto;

import lombok.Data;

@Data
public class PasswordForm {

    private String newPassword;

    private String newPasswordConfirm;
}
