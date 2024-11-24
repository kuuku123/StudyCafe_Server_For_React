package com.StudyCafe_R.modules.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountStudyMembers is a Querydsl query type for AccountStudyMembers
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountStudyMembers extends EntityPathBase<AccountStudyMembers> {

    private static final long serialVersionUID = 1793027229L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountStudyMembers accountStudyMembers = new QAccountStudyMembers("accountStudyMembers");

    public final QAccount account;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.StudyCafe_R.modules.study.domain.QStudy study;

    public QAccountStudyMembers(String variable) {
        this(AccountStudyMembers.class, forVariable(variable), INITS);
    }

    public QAccountStudyMembers(Path<? extends AccountStudyMembers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountStudyMembers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountStudyMembers(PathMetadata metadata, PathInits inits) {
        this(AccountStudyMembers.class, metadata, inits);
    }

    public QAccountStudyMembers(Class<? extends AccountStudyMembers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.study = inits.isInitialized("study") ? new com.StudyCafe_R.modules.study.domain.QStudy(forProperty("study")) : null;
    }

}

