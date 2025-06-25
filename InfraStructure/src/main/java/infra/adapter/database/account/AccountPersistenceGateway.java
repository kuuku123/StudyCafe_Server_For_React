package infra.adapter.database.account;


import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import infra.adapter.database.account.mapper.AccountMapper;
import infra.adapter.database.tag.TagEntity;
import infra.adapter.database.tag.TagRepository;
import infra.adapter.database.zone.ZoneEntity;
import infra.adapter.database.zone.ZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountPersistenceGateway implements AccountPersistenceOperationsOutputPort {

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
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

        syncAccountZoneAndTag(account, accountEntity);
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
        return accountRepository.findByEmail(email)
                .map(accountEntity ->  accountMapper.mapToDomain(accountEntity));
    }

    @Override
    public Optional<Account> findByNickname(String emailOrNickname) {
        return Optional.empty();
    }

    private void syncAccountZoneAndTag(Account account, AccountEntity accountEntity) {
        accountEntity.getAccountTagSet().clear();
        Set<Long> tags = account.getTags();
        for (Long tag : tags) {
            TagEntity tagEntity = tagRepository.findById(tag)
                    .orElseThrow(() -> new EntityNotFoundException("Tag not Found: " + tag));
            AccountTagEntity accountTagEntity= AccountTagEntity.builder()
                    .tag(tagEntity)
                    .build();
            accountEntity.addAccountTagEntity(accountTagEntity);
        }

        accountEntity.getAccountZoneSet().clear();
        Set<Long> zones = account.getZones();
        for (Long zone : zones) {
            ZoneEntity zoneEntity = zoneRepository.findById(zone)
                    .orElseThrow(() -> new EntityNotFoundException("Zone not Found: " + zone));
            AccountZoneEntity accountZoneEntity= AccountZoneEntity.builder()
                    .zone(zoneEntity)
                    .build();
            accountEntity.addAccountZoneEntity(accountZoneEntity);
        }
    }
}
