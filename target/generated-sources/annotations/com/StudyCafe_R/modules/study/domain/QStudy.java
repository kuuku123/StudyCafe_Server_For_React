package com.StudyCafe_R.modules.study.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudy is a Querydsl query type for Study
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudy extends EntityPathBase<Study> {

    private static final long serialVersionUID = -585492307L;

    public static final QStudy study = new QStudy("study");

    public final BooleanPath closed = createBoolean("closed");

    public final DateTimePath<java.time.LocalDateTime> closedDateTime = createDateTime("closedDateTime", java.time.LocalDateTime.class);

    public final StringPath fullDescription = createString("fullDescription");

    public final StringPath fullDescriptionText = createString("fullDescriptionText");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final SetPath<com.StudyCafe_R.modules.account.domain.AccountStudyManager, com.StudyCafe_R.modules.account.domain.QAccountStudyManager> managers = this.<com.StudyCafe_R.modules.account.domain.AccountStudyManager, com.StudyCafe_R.modules.account.domain.QAccountStudyManager>createSet("managers", com.StudyCafe_R.modules.account.domain.AccountStudyManager.class, com.StudyCafe_R.modules.account.domain.QAccountStudyManager.class, PathInits.DIRECT2);

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final SetPath<com.StudyCafe_R.modules.account.domain.AccountStudyMembers, com.StudyCafe_R.modules.account.domain.QAccountStudyMembers> members = this.<com.StudyCafe_R.modules.account.domain.AccountStudyMembers, com.StudyCafe_R.modules.account.domain.QAccountStudyMembers>createSet("members", com.StudyCafe_R.modules.account.domain.AccountStudyMembers.class, com.StudyCafe_R.modules.account.domain.QAccountStudyMembers.class, PathInits.DIRECT2);

    public final StringPath path = createString("path");

    public final BooleanPath published = createBoolean("published");

    public final DateTimePath<java.time.LocalDateTime> publishedDateTime = createDateTime("publishedDateTime", java.time.LocalDateTime.class);

    public final BooleanPath recruiting = createBoolean("recruiting");

    public final DateTimePath<java.time.LocalDateTime> recruitingUpdatedDateTime = createDateTime("recruitingUpdatedDateTime", java.time.LocalDateTime.class);

    public final StringPath shortDescription = createString("shortDescription");

    public final ArrayPath<byte[], Byte> studyImage = createArray("studyImage", byte[].class);

    public final SetPath<StudyTag, QStudyTag> tags = this.<StudyTag, QStudyTag>createSet("tags", StudyTag.class, QStudyTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final BooleanPath useBanner = createBoolean("useBanner");

    public final SetPath<StudyZone, QStudyZone> zones = this.<StudyZone, QStudyZone>createSet("zones", StudyZone.class, QStudyZone.class, PathInits.DIRECT2);

    public QStudy(String variable) {
        super(Study.class, forVariable(variable));
    }

    public QStudy(Path<? extends Study> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStudy(PathMetadata metadata) {
        super(Study.class, metadata);
    }

}

