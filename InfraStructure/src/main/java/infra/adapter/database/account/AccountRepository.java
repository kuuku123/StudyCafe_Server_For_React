package infra.adapter.database.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface AccountRepository extends JpaRepository<AccountEntity,Long>, QuerydslPredicateExecutor<AccountEntity> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findByNickname(String emailOrNickname);
}
