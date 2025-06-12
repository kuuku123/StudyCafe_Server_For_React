package com.StudyCafe_R.usecase.account.command;

public record UpdateNotificationCommand(
        Long accountId,
        boolean studyCreatedByEmail,
        boolean studyCreatedByWeb,
        boolean studyEnrollmentResultByEmail,
        boolean studyEnrollmentResultByWeb,
        boolean studyUpdatedByEmail,
        boolean studyUpdatedByWeb
) {
}
