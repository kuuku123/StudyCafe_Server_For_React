package com.StudyCafe_R.modules.study;

import com.StudyCafe_R.modules.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    Page<Study> findByKeyword(String keyword, Pageable pageable);
}
