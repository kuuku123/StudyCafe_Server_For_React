package com.StudyCafe_R.modules.notification.repository;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long>, NotificationRepositoryExtension {
    long countByAccountAndChecked(Account account, boolean b);

//    List<Notification> findByAccountAndIsReadAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean isRead, boolean checked);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean checked);
}
