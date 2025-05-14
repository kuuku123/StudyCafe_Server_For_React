package com.StudyCafe_R.account.usecase;


import lombok.Data;

@Data
public class CreateAccountCommand {

    private String nickname;
    private String email;

}
