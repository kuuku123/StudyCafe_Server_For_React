package com.StudyCafe_R.modules.study.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyTag is a Querydsl query type for StudyTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyTag extends EntityPathBase<StudyTag> {

    private static final long serialVersionUID = -539044947L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyTag studyTag = new QStudyTag("studyTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QStudy study;

    public final com.StudyCafe_R.modules.tag.QTag tag;

    public QStudyTag(String variable) {
        this(StudyTag.class, forVariable(variable), INITS);
    }

    public QStudyTag(Path<? extends StudyTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyTag(PathMetadata metadata, PathInits inits) {
        this(StudyTag.class, metadata, inits);
    }

    public QStudyTag(Class<? extends StudyTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study")) : null;
        this.tag = inits.isInitialized("tag") ? new com.StudyCafe_R.modules.tag.QTag(forProperty("tag")) : null;
    }

}

