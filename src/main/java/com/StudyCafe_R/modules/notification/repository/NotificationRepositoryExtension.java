package com.StudyCafe_R.modules.notification.repository;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.Notification;

import java.util.List;

public interface NotificationRepositoryExtension {

    List<Notification> findByAccountAndChecked(Account account, boolean checked);
}
