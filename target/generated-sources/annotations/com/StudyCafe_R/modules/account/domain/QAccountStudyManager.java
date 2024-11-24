package com.StudyCafe_R.modules.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountStudyManager is a Querydsl query type for AccountStudyManager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountStudyManager extends EntityPathBase<AccountStudyManager> {

    private static final long serialVersionUID = 1679405873L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountStudyManager accountStudyManager = new QAccountStudyManager("accountStudyManager");

    public final QAccount account;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.StudyCafe_R.modules.study.domain.QStudy study;

    public QAccountStudyManager(String variable) {
        this(AccountStudyManager.class, forVariable(variable), INITS);
    }

    public QAccountStudyManager(Path<? extends AccountStudyManager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountStudyManager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountStudyManager(PathMetadata metadata, PathInits inits) {
        this(AccountStudyManager.class, metadata, inits);
    }

    public QAccountStudyManager(Class<? extends AccountStudyManager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.study = inits.isInitialized("study") ? new com.StudyCafe_R.modules.study.domain.QStudy(forProperty("study")) : null;
    }

}

