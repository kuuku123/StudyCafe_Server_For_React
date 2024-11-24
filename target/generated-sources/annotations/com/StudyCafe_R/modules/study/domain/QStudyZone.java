package com.StudyCafe_R.modules.study.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyZone is a Querydsl query type for StudyZone
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyZone extends EntityPathBase<StudyZone> {

    private static final long serialVersionUID = 469668345L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyZone studyZone = new QStudyZone("studyZone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QStudy study;

    public final com.StudyCafe_R.modules.zone.QZone zone;

    public QStudyZone(String variable) {
        this(StudyZone.class, forVariable(variable), INITS);
    }

    public QStudyZone(Path<? extends StudyZone> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyZone(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyZone(PathMetadata metadata, PathInits inits) {
        this(StudyZone.class, metadata, inits);
    }

    public QStudyZone(Class<? extends StudyZone> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study")) : null;
        this.zone = inits.isInitialized("zone") ? new com.StudyCafe_R.modules.zone.QZone(forProperty("zone")) : null;
    }

}

