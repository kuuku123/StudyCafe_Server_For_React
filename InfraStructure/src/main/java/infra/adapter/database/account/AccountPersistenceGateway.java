package infra.adapter.database.account;


import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountPersistenceGateway implements AccountPersistenceOperationsOutputPort {

    private final AccountRepository accountRepository;

    @Override
    public void save(Account account) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return false;
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByNickname(String emailOrNickname) {
        return Optional.empty();
    }
}
