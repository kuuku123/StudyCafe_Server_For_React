package com.StudyCafe_R.modules.account.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginForm {

    @NotBlank
    private String nicknameOrEmail;

    @NotBlank
    @Length(min = 1, max = 50)
    private String password;
}
