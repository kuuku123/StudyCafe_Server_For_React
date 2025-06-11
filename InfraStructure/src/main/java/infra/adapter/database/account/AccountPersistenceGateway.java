package infra.adapter.database.account;


import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import infra.adapter.database.account.mapper.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountPersistenceGateway implements AccountPersistenceOperationsOutputPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public void save(Account account) {
        final AccountEntity accountEntity;

        if (account.getId() == null) {
            // --- CREATE new account ---
            // The domain object is new, so we map it to a new entity.
            accountEntity = accountMapper.mapToEntity(account);
        } else {
            // --- UPDATE existing account ---
            // The domain object represents an existing account.
            // First, fetch the managed entity from the database.
            accountEntity = accountRepository.findById(account.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found for update with ID: " + account.getId()));

            // Then, update its state from the domain object.
            accountMapper.updateEntityFromDomain(accountEntity, account);
        }

        accountRepository.save(accountEntity);
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
