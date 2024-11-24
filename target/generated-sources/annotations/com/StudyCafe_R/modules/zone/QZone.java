package com.StudyCafe_R.modules.zone;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QZone is a Querydsl query type for Zone
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QZone extends EntityPathBase<Zone> {

    private static final long serialVersionUID = 2042148357L;

    public static final QZone zone = new QZone("zone");

    public final SetPath<com.StudyCafe_R.modules.account.domain.AccountZone, com.StudyCafe_R.modules.account.domain.QAccountZone> accountZoneSet = this.<com.StudyCafe_R.modules.account.domain.AccountZone, com.StudyCafe_R.modules.account.domain.QAccountZone>createSet("accountZoneSet", com.StudyCafe_R.modules.account.domain.AccountZone.class, com.StudyCafe_R.modules.account.domain.QAccountZone.class, PathInits.DIRECT2);

    public final StringPath city = createString("city");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath localNameOfCity = createString("localNameOfCity");

    public final StringPath province = createString("province");

    public final SetPath<com.StudyCafe_R.modules.study.domain.StudyZone, com.StudyCafe_R.modules.study.domain.QStudyZone> studyZoneSet = this.<com.StudyCafe_R.modules.study.domain.StudyZone, com.StudyCafe_R.modules.study.domain.QStudyZone>createSet("studyZoneSet", com.StudyCafe_R.modules.study.domain.StudyZone.class, com.StudyCafe_R.modules.study.domain.QStudyZone.class, PathInits.DIRECT2);

    public QZone(String variable) {
        super(Zone.class, forVariable(variable));
    }

    public QZone(Path<? extends Zone> path) {
        super(path.getType(), path.getMetadata());
    }

    public QZone(PathMetadata metadata) {
        super(Zone.class, metadata);
    }

}

