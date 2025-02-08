package com.StudyCafe_R.modules.account.responseDto;

import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDto {

    private String nickname;
    private String bio;
    private String url;
    private String occupation;
    private String location;
    private String email;
    private boolean emailVerified;
    private String profileImage;
    private List<TagDto> tags = new ArrayList<>();
    private List<ZoneDto> zones = new ArrayList<>();

}
