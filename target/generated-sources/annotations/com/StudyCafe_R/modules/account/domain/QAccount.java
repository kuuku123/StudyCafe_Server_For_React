package com.StudyCafe_R.modules.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = 778904813L;

    public static final QAccount account = new QAccount("account");

    public final SetPath<AccountTag, QAccountTag> accountTagSet = this.<AccountTag, QAccountTag>createSet("accountTagSet", AccountTag.class, QAccountTag.class, PathInits.DIRECT2);

    public final SetPath<AccountZone, QAccountZone> accountZoneSet = this.<AccountZone, QAccountZone>createSet("accountZoneSet", AccountZone.class, QAccountZone.class, PathInits.DIRECT2);

    public final StringPath bio = createString("bio");

    public final StringPath createdOrMergedSocialProviders = createString("createdOrMergedSocialProviders");

    public final StringPath email = createString("email");

    public final StringPath emailCheckToken = createString("emailCheckToken");

    public final DateTimePath<java.time.LocalDateTime> emailCheckTokenGeneratedAt = createDateTime("emailCheckTokenGeneratedAt", java.time.LocalDateTime.class);

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final SetPath<com.StudyCafe_R.modules.event.domain.Enrollment, com.StudyCafe_R.modules.event.domain.QEnrollment> enrollments = this.<com.StudyCafe_R.modules.event.domain.Enrollment, com.StudyCafe_R.modules.event.domain.QEnrollment>createSet("enrollments", com.StudyCafe_R.modules.event.domain.Enrollment.class, com.StudyCafe_R.modules.event.domain.QEnrollment.class, PathInits.DIRECT2);

    public final SetPath<com.StudyCafe_R.modules.event.domain.Event, com.StudyCafe_R.modules.event.domain.QEvent> events = this.<com.StudyCafe_R.modules.event.domain.Event, com.StudyCafe_R.modules.event.domain.QEvent>createSet("events", com.StudyCafe_R.modules.event.domain.Event.class, com.StudyCafe_R.modules.event.domain.QEvent.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final StringPath location = createString("location");

    public final SetPath<AccountStudyManager, QAccountStudyManager> managers = this.<AccountStudyManager, QAccountStudyManager>createSet("managers", AccountStudyManager.class, QAccountStudyManager.class, PathInits.DIRECT2);

    public final SetPath<AccountStudyManager, QAccountStudyManager> members = this.<AccountStudyManager, QAccountStudyManager>createSet("members", AccountStudyManager.class, QAccountStudyManager.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath occupation = createString("occupation");

    public final StringPath password = createString("password");

    public final ArrayPath<byte[], Byte> profileImage = createArray("profileImage", byte[].class);

    public final BooleanPath studyCreatedByEmail = createBoolean("studyCreatedByEmail");

    public final BooleanPath studyCreatedByWeb = createBoolean("studyCreatedByWeb");

    public final BooleanPath studyEnrollmentResultByEmail = createBoolean("studyEnrollmentResultByEmail");

    public final BooleanPath studyEnrollmentResultByWeb = createBoolean("studyEnrollmentResultByWeb");

    public final BooleanPath studyUpdatedByEmail = createBoolean("studyUpdatedByEmail");

    public final BooleanPath studyUpdatedByWeb = createBoolean("studyUpdatedByWeb");

    public final NumberPath<java.math.BigInteger> subSocialIdentifier = createNumber("subSocialIdentifier", java.math.BigInteger.class);

    public final StringPath url = createString("url");

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

