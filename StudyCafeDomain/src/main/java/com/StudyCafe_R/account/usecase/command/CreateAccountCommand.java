package com.StudyCafe_R.account.usecase.command;


import lombok.Data;

@Data
public class CreateAccountCommand {

    private String nickname;
    private String email;

    public CreateAccountCommand(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
