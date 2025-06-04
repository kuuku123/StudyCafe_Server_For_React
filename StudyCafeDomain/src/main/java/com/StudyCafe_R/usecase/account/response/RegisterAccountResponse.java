package com.StudyCafe_R.usecase.account.response;

public record RegisterAccountResponse(
        String nickname,
        String email,
        String profileImage
) {}