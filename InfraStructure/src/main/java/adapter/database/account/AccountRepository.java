package adapter.database.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AccountRepository extends JpaRepository<AccountEntity,Long>, QuerydslPredicateExecutor<AccountEntity> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    AccountEntity findByEmail(String email);

    AccountEntity findByNickname(String emailOrNickname);
}
