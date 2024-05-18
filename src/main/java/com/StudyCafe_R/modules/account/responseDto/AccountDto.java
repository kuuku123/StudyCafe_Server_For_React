package com.StudyCafe_R.modules.account.responseDto;

import com.StudyCafe_R.modules.account.form.Profile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountDto {

    private String bio;
    private String url;
    private String occupation;
    private String location;
    private String email;
    private boolean emailVerified;

}
