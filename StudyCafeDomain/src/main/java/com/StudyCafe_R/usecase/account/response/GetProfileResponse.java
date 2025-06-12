package com.StudyCafe_R.usecase.account.response;

import com.StudyCafe_R.usecase.tag.response.TagResponse;
import com.StudyCafe_R.usecase.zone.response.ZoneResponse;

import java.util.List;

public record GetProfileResponse(
        Long accountId,
        String email,
        String nickname,
        String bio,
        String url,
        String occupation,
        String location,
        String profileImage,          // base64 or URL
        List<TagResponse> tags,
        List<ZoneResponse> zones
) {}