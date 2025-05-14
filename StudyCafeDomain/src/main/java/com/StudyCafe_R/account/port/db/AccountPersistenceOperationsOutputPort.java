package com.StudyCafe_R.account.port.db;

import com.StudyCafe_R.account.domain.Account;

import java.util.Optional;

public interface AccountPersistenceOperationsOutputPort {

    void save (Account account);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findById(Long accountId);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String emailOrNickname);

}
