package com.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.modules.study.domain.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private Study study;

    public StudyCreatedEvent(Study study) {
        this.study = study;
    }

}
