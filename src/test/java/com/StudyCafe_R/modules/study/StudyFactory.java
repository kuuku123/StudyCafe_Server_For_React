package com.StudyCafe_R.modules.study;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyFactory {

    @Autowired
    StudyService studyService;
    public Study createStudy(String path, Account manager) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study,manager);
        return study;
    }
}
