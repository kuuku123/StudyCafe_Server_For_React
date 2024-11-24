package com.StudyCafe_R.modules.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTag is a Querydsl query type for Tag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTag extends EntityPathBase<Tag> {

    private static final long serialVersionUID = 1258164765L;

    public static final QTag tag = new QTag("tag");

    public final SetPath<com.StudyCafe_R.modules.account.domain.AccountTag, com.StudyCafe_R.modules.account.domain.QAccountTag> accountTagSet = this.<com.StudyCafe_R.modules.account.domain.AccountTag, com.StudyCafe_R.modules.account.domain.QAccountTag>createSet("accountTagSet", com.StudyCafe_R.modules.account.domain.AccountTag.class, com.StudyCafe_R.modules.account.domain.QAccountTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.StudyCafe_R.modules.study.domain.StudyTag, com.StudyCafe_R.modules.study.domain.QStudyTag> studyTagSet = this.<com.StudyCafe_R.modules.study.domain.StudyTag, com.StudyCafe_R.modules.study.domain.QStudyTag>createSet("studyTagSet", com.StudyCafe_R.modules.study.domain.StudyTag.class, com.StudyCafe_R.modules.study.domain.QStudyTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QTag(String variable) {
        super(Tag.class, forVariable(variable));
    }

    public QTag(Path<? extends Tag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTag(PathMetadata metadata) {
        super(Tag.class, metadata);
    }

}

