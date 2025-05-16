package com.StudyCafe_R.account.usecase.command;

import lombok.Data;

@Data
public class UpdateProfileCommand {

    private Long accountId;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    private String profileImage;

}
