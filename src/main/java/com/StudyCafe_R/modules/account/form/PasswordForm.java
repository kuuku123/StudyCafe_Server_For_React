package com.StudyCafe_R.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {

    private String newPassword;

    private String newPasswordConfirm;
}
