package com.StudyCafe_R.modules.event.event;

import com.StudyCafe_R.modules.event.domain.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    protected final Enrollment enrollment;
    protected final String message;
}
