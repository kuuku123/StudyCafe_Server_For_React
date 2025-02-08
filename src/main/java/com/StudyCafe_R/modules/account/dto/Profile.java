package com.StudyCafe_R.modules.account.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Profile {

    @Length(max=5)
    private String bio;

    @Length(max=50)
    private String url;

    @Length(max=50)
    private String occupation;

    @Length(max=50)
    private String location; // varchar(255) above all info

    private String profileImage;
}
