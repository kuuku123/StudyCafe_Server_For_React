package com.StudyCafe_R.account.usecase;

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
