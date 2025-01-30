package com.StudyCafe_R.modules.notification.repository;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.Notification;
import com.StudyCafe_R.modules.notification.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

public class NotificationRepositoryCustomImpl extends QuerydslRepositorySupport implements NotificationRepositoryExtension {

    private final JPAQueryFactory jpaQueryFactory;

    public NotificationRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Notification.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Notification> findByAccountAndChecked(Account account,  boolean checked) {
        QNotification notification = QNotification.notification; // Q-Class for Notification entity

        return jpaQueryFactory
                .selectFrom(notification)
                .where(
                        notification.account.eq(account),
                        notification.checked.eq(checked)
                )
                .orderBy(notification.createdDateTime.desc())
                .fetch();
    }

}
