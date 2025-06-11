package infra.adapter.database.account.mapper;
import com.StudyCafe_R.domain.Account;
import infra.adapter.database.account.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    /**
     * Maps a domain Account object to a new AccountEntity object.
     * This is typically used when creating a new account.
     * @param account The domain object.
     * @return A new AccountEntity.
     */
    public AccountEntity mapToEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId()) // Can be null for new entities
                .email(account.getEmail())
                .nickname(account.getNickname())
                .bio(account.getBio())
                .url(account.getUrl())
                .occupation(account.getOccupation())
                .location(account.getLocation())
                .profileImage(account.getProfileImage())
                .studyCreatedByEmail(account.isStudyCreatedByEmail())
                .studyCreatedByWeb(account.isStudyCreatedByWeb())
                .studyEnrollmentResultByEmail(account.isStudyEnrollmentResultByEmail())
                .studyEnrollmentResultByWeb(account.isStudyEnrollmentResultByWeb())
                .studyUpdatedByEmail(account.isStudyUpdatedByEmail())
                .studyUpdatedByWeb(account.isStudyUpdatedByWeb())
                // Note: Collections like tags and zones are handled separately
                // as they require fetching related entities.
                .build();
    }

    /**
     * Updates an existing AccountEntity with data from a domain Account object.
     * This is crucial for update operations to ensure you're working with a managed JPA entity.
     * @param entity The managed JPA entity to update.
     * @param domain The domain object with the new state.
     */
    public void updateEntityFromDomain(AccountEntity entity, Account domain) {
        entity.setEmail(domain.getEmail());
        entity.setNickname(domain.getNickname());
        entity.setBio(domain.getBio());
        entity.setUrl(domain.getUrl());
        entity.setOccupation(domain.getOccupation());
        entity.setLocation(domain.getLocation());
        entity.setProfileImage(domain.getProfileImage());
        entity.setStudyCreatedByEmail(domain.isStudyCreatedByEmail());
        entity.setStudyCreatedByWeb(domain.isStudyCreatedByWeb());
        entity.setStudyEnrollmentResultByEmail(domain.isStudyEnrollmentResultByEmail());
        entity.setStudyEnrollmentResultByWeb(domain.isStudyEnrollmentResultByWeb());
        entity.setStudyUpdatedByEmail(domain.isStudyUpdatedByEmail());
        entity.setStudyUpdatedByWeb(domain.isStudyUpdatedByWeb());

        // Complex logic for collections will be added here later.
    }

    /**
     * Maps a persisted AccountEntity back to a domain Account object.
     * @param entity The JPA entity from the database.
     * @return A domain Account object.
     */
    public Account mapToDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return Account.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .bio(entity.getBio())
                .url(entity.getUrl())
                .occupation(entity.getOccupation())
                .location(entity.getLocation())
                // You would map other fields and collections back here as needed.
                .build();
    }
}
