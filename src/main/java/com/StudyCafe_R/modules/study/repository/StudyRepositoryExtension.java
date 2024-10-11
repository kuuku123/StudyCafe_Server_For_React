package com.StudyCafe_R.modules.study.repository;

import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    Page<Study> findStudyByKeyword(String keyword, Pageable pageable);

    Page<Study> findStudyByZonesAndTags(List<Tag> tags, List<Zone> zones, Pageable pageable);
}
