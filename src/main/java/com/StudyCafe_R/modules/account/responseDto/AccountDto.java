package com.StudyCafe_R.modules.account.responseDto;

import com.StudyCafe_R.modules.account.form.Profile;
import lombok.Data;

@Data
public class AccountDto {

    private Profile profile;
    private String email;
}
