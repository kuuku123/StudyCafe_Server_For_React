package com.StudyCafe_R.usecase.account.command;

import lombok.Data;

public record UpdateProfileCommand(
        Long accountId,
        String bio,
        String url,
        String occupation,
        String location,
        String profileImage
) {
}

