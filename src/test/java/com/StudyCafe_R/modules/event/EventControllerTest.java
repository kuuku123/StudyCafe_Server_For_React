package com.StudyCafe_R.modules.event;

import com.StudyCafe_R.StudyCafe_R.modules.event.domain.EventType;
import com.StudyCafe_R.infra.AbstractContainerBaseTest;
import com.StudyCafe_R.infra.MockMvcTest;
import com.StudyCafe_R.modules.account.AccountFactory;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.event.domain.Event;
import com.StudyCafe_R.modules.study.StudyFactory;
import com.StudyCafe_R.modules.study.domain.Study;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EventControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudyFactory studyFactory;

    @Autowired
    AccountFactory accountFactory;
    @Autowired
    EventService eventService;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;
    @BeforeEach
    void beforeEach() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setNickname("tony");
        signUpRequest.setEmail("tony@email.com");
        accountService.processNewAccount(signUpRequest);

    }
    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account tony = accountFactory.createAccount("test-tony");
        Study study = studyFactory.createStudy("test-study", tony);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, tony);

        mockMvc.perform(post("/study/" + study.getEncodedPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        Account repoTony = accountRepository.findByNickname("tony");
        isAccepted(repoTony,event);
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 대기중 ( 이미 인원이 꽉차서 대기)")
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account tony = accountFactory.createAccount("test-tony");
        Study study = studyFactory.createStudy("test-study", tony);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, tony);

        Account may = accountFactory.createAccount("may");
        Account june = accountFactory.createAccount("june");
        eventService.newEnrollment(event,may);
        eventService.newEnrollment(event,june);

        mockMvc.perform(post("/study/" + study.getEncodedPath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        Account repoTony = accountRepository.findByNickname("tony");
        isNotAccepted(repoTony,event);
    }


    @Test
    @DisplayName("참가신청 확정자가 선착순 모임에 참가 신청을 취소하는 경우 , 바로 다음 대기자를 자동으로 신청 확인한다.")
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account repoTony = accountRepository.findByNickname("tony");
        Account tony = accountFactory.createAccount("test-tony");
        Account may = accountFactory.createAccount("may");
        Study study = studyFactory.createStudy("test-study", tony);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, tony);

        eventService.newEnrollment(event,may);
        eventService.newEnrollment(event,tony);
        eventService.newEnrollment(event,repoTony);

        mockMvc.perform(post("/study/" + study.getEncodedPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        isAccepted(may,event);
        isAccepted(tony,event);
        assertNull(enrollmentRepository.findByEventAndAccount(event,repoTony));
    }

    @Test
    @DisplayName("참가신청 비확정자가 선착순 모임에 참가 신청을 취소하는 경우 , 기존 확정자를 그대로 유지하고 새로운 확정자는 없다.")
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void not_accepted_account_cacnelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account repoTony = accountRepository.findByNickname("tony");
        Account tony = accountFactory.createAccount("test-tony");
        Account may = accountFactory.createAccount("may");
        Study study = studyFactory.createStudy("test-study", tony);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, tony);

        eventService.newEnrollment(event,may);
        eventService.newEnrollment(event,tony);
        eventService.newEnrollment(event,repoTony);

        isAccepted(may,event);
        isAccepted(tony,event);
        isNotAccepted(repoTony,event);

        mockMvc.perform(post("/study/" + study.getEncodedPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

        isAccepted(may,event);
        isAccepted(tony,event);
        assertNull(enrollmentRepository.findByEventAndAccount(event,repoTony));

    }

    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());

        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event,study,account);
    }

    private void isNotAccepted(Account account, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event,account).isAccepted());
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event,account).isAccepted());
    }
}