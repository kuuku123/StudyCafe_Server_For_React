package com.StudyCafe_R.modules.study.repository;

import com.StudyCafe_R.modules.account.domain.QAccountStudyMembers;
import com.StudyCafe_R.modules.study.domain.QStudy;
import com.StudyCafe_R.modules.study.domain.QStudyTag;
import com.StudyCafe_R.modules.study.domain.QStudyZone;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    private final JPAQueryFactory jpaQueryFactory;

    public StudyRepositoryExtensionImpl(JPAQueryFactory jpaQueryFactory) {
        super(Study.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Study> findStudyByKeyword(String keyword, Pageable pageable) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                        .and(study.title.containsIgnoreCase(keyword))
                        .or(study.tags.any().tag.title.containsIgnoreCase(keyword))
                        .or(study.zones.any().zone.localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QStudyTag.studyTag).where(QStudyTag.studyTag.tag.title.like(keyword)).fetchJoin()
                .leftJoin(study.members, QAccountStudyMembers.accountStudyMembers).fetchJoin();
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());

    }

    @Override
    public Page<Study> findStudyByZonesAndTags(List<Tag> tags, List<Zone> zones, Pageable pageable) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study)
                .innerJoin(study.tags, QStudyTag.studyTag).fetchJoin()
                .on(study.tags.any().tag.in(tags))
                .innerJoin(study.zones, QStudyZone.studyZone).fetchJoin()
                .on(study.zones.any().zone.in(zones));
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
