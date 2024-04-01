package com.StudyCafe_R.modules.notification;

import com.StudyCafe_R.modules.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    long countByAccountAndChecked(Account account, boolean b);

    List<Notification> findByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean checked);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean checked);
}
