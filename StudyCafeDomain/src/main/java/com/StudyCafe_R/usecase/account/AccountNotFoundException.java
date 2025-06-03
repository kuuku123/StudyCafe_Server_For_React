package com.StudyCafe_R.usecase.account;

public class AccountNotFoundException extends RuntimeException{

    private final Long accountId;

    /** If you want to include the missing account’s ID. */
    public AccountNotFoundException(Long accountId, String message) {
        super(message);
        this.accountId = accountId;
    }
}
