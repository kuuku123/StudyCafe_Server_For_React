package com.StudyCafe_R.modules.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountTag is a Querydsl query type for AccountTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountTag extends EntityPathBase<AccountTag> {

    private static final long serialVersionUID = -1354932371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountTag accountTag = new QAccountTag("accountTag");

    public final QAccount account;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.StudyCafe_R.modules.tag.QTag tag;

    public QAccountTag(String variable) {
        this(AccountTag.class, forVariable(variable), INITS);
    }

    public QAccountTag(Path<? extends AccountTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountTag(PathMetadata metadata, PathInits inits) {
        this(AccountTag.class, metadata, inits);
    }

    public QAccountTag(Class<? extends AccountTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.tag = inits.isInitialized("tag") ? new com.StudyCafe_R.modules.tag.QTag(forProperty("tag")) : null;
    }

}

