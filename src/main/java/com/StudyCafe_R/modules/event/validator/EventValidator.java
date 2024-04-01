package com.StudyCafe_R.modules.event.validator;

import com.StudyCafe_R.StudyCafe_R.modules.event.form.EventForm;
import com.StudyCafe_R.modules.event.domain.Event;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventForm eventForm = (EventForm) target;

        if(isNotValidEndEnrollmentDateTime(eventForm)){
            errors.rejectValue("endEnrollmentDateTime", "wrong.datetime","모입 접수 종료일시를 정확히 입력하세요");
        }

        if (isNotValidEndDateTime(eventForm)) {
            errors.rejectValue("endDateTime", "wrong.datetime","모입 접수 종료일시를 정확히 입력하세요");
        }

        if(isNotValidStartDateTime(eventForm)) {
            errors.rejectValue("startDateTime", "wrong.datetime","모입 접수 시작일시를 정확히 입력하세요");
        }

    }

    private boolean isNotValidStartDateTime(EventForm eventForm) {
        LocalDateTime startDateTime = eventForm.getStartDateTime();
        return startDateTime.isBefore(eventForm.getEndEnrollmentDateTime());
    }

    private boolean isNotValidEndEnrollmentDateTime(EventForm eventForm) {
        return eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isNotValidEndDateTime(EventForm eventForm) {
        return eventForm.getEndDateTime().isBefore(eventForm.getStartDateTime()) || eventForm.getEndDateTime().isBefore(eventForm.getEndEnrollmentDateTime());
    }

    public void validateUpdateForm(EventForm eventForm, Event event, Errors errors) {
        if (eventForm.getLimitOfEnrollments() < event.getNumberOfAcceptedEnrollments()) {
            errors.rejectValue("limitOfEnrollments","wrong.value","확인된 참가 신청보다 모집 인원수가 더 커야합니다.");
        }
    }
}
