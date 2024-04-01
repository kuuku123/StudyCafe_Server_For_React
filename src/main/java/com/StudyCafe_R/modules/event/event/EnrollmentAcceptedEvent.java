package com.StudyCafe_R.modules.event.event;


import com.StudyCafe_R.modules.event.domain.Enrollment;

public class EnrollmentAcceptedEvent extends EnrollmentEvent {

    public EnrollmentAcceptedEvent(Enrollment enrollment) {
        super(enrollment, "모임참가 신청을 확인했습니다. 모임에 참석하세요..");
    }
}
