package com.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.modules.notification.NotificationType;
import com.StudyCafe_R.infra.config.AppProperties;
import com.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.modules.account.AccountPredicates;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.domain.AccountStudyMembers;
import com.StudyCafe_R.modules.notification.Notification;
import com.StudyCafe_R.modules.notification.NotificationRepository;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyUpdateEvent(StudyUpdateEvent studyUpdateEvent) {
        Study study = studyRepository.findStudyWithManagersAndMembersById(studyUpdateEvent.getStudy().getId());
        Set<Account> accounts = new HashSet<>();
        accounts.addAll(study.getManagers().stream().map(AccountStudyManager::getAccount).collect(Collectors.toSet()));
        accounts.addAll(study.getMembers().stream().map(AccountStudyMembers::getAccount).collect(Collectors.toSet()));
        accounts.forEach(account -> {
            if (account.isStudyUpdatedByEmail()) {
                sendStudyCreatedEmail(study, account, studyUpdateEvent.getMessage(), "스터디 카페 '" + study.getTitle() + "' 스터디에 새소식이 있습니다...");
            }
            if (account.isStudyUpdatedByWeb()) {
                createNotification(study, account, studyUpdateEvent.getMessage(), NotificationType.STUDY_UPDATED);
            }
        });
    }

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyRepository.findStudyWithTagsAndZoneById(studyCreatedEvent.getStudy().getId());
        Set<Tag> tags = study.getTags().stream().map(StudyTag::getTag).collect(Collectors.toSet());
        Set<Zone> zones = study.getZones().stream().map(StudyZone::getZone).collect(Collectors.toSet());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(tags, zones));
        accounts.forEach(account -> {
            if (account.isStudyCreatedByEmail()) {
                sendStudyCreatedEmail(study, account, "새로운 스터디가 생겼습니다.", "스터디카페, '" + study.getTitle() + "' 스터디가 생겼습니다.");
            }

            if (account.isStudyCreatedByWeb()) {
                createNotification(study, account, study.getShortDescription(), NotificationType.STUDY_CREATED);
            }
        });
    }

    private void createNotification(Study study, Account account, String message, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setAccount(account);
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }

    private void sendStudyCreatedEmail(Study study, Account account, String contextMessage, String emailSubject) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodedPath());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", contextMessage);
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("email/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject(emailSubject)
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
