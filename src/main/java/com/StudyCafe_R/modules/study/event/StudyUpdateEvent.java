package com.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.modules.study.domain.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudyUpdateEvent {

    private final Study study;
    private final String message;

}
