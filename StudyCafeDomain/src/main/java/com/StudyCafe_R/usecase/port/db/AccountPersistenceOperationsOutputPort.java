package com.StudyCafe_R.usecase.port.db;

import com.StudyCafe_R.domain.Account;

import java.util.Optional;

public interface AccountPersistenceOperationsOutputPort {

    void save (Account account);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findById(Long accountId);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String emailOrNickname);

}
