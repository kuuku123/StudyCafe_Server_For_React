package com.StudyCafe_R.account.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {


    @Test
    void create_Account() {
        Account aDefault = Account.builder()
                .id(1L)
                .bio("default")
                .build();

        System.out.println("aDefault = " + aDefault);

    }
}