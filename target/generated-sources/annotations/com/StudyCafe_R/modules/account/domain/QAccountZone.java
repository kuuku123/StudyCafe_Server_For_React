package com.StudyCafe_R.modules.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountZone is a Querydsl query type for AccountZone
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountZone extends EntityPathBase<AccountZone> {

    private static final long serialVersionUID = 946961977L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountZone accountZone = new QAccountZone("accountZone");

    public final QAccount account;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.StudyCafe_R.modules.zone.QZone zone;

    public QAccountZone(String variable) {
        this(AccountZone.class, forVariable(variable), INITS);
    }

    public QAccountZone(Path<? extends AccountZone> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountZone(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountZone(PathMetadata metadata, PathInits inits) {
        this(AccountZone.class, metadata, inits);
    }

    public QAccountZone(Class<? extends AccountZone> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.zone = inits.isInitialized("zone") ? new com.StudyCafe_R.modules.zone.QZone(forProperty("zone")) : null;
    }

}

