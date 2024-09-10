package com.StudyCafe_R.modules.account;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account tony = new Account();
        tony.setNickname(nickname);
        tony.setEmail(nickname + "@email.com");
        accountRepository.save(tony);
        return tony;
    }
}
