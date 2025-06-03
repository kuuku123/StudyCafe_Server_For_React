package com.StudyCafe_R.account.domain;

import com.StudyCafe_R.domain.Account;
import org.junit.jupiter.api.Test;

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