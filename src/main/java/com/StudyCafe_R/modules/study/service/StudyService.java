package com.StudyCafe_R.modules.study.service;

import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.study.exceptions.NotManagerException;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.form.StudyDescriptionForm;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.domain.AccountStudyMembers;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.event.StudyCreatedEvent;
import com.StudyCafe_R.modules.study.event.StudyUpdateEvent;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyConfigService studyConfigService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final TagRepository tagRepository;

    public Study createNewStudy(StudyForm studyForm, Account account) {
        Study study = modelMapper.map(studyForm, Study.class);
        studyConfigService.updateStudyImage(study, studyForm);
        Study newStudy = studyRepository.save(study);
        AccountStudyManager accountStudyManager = AccountStudyManager.builder()
                .account(account)
                .study(study)
                .build();

        newStudy.addManager(accountStudyManager);
        return newStudy;
    }

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        AccountStudyManager accountStudyManager = AccountStudyManager.builder()
                .account(account)
                .study(study)
                .build();

        newStudy.addManager(accountStudyManager);
        return newStudy;
    }


    public Study getStudyToUpdate(Account account, String path) {
        Study study = getStudy(path);
        checkIfManager(account, study);
        return study;
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        Study study = studyRepository.findStudyWithManagersByPath(path);
        checkIfManager(account, study);
        return study;
    }

    private void checkIfManager(Account account, Study study) {
        if (!study.isManagedby(account)) {
            throw new NotManagerException("해당 기능을 사용할 수 없습니다.");
        }
    }

    public Study getStudy(String path) {
        Study study = studyRepository.findByPath(path);
        return study;
    }

    public StudyDto getStudyDto(String path) {
        Study study = studyRepository.findByPath(path);
        StudyDto studyDto = modelMapper.map(study, StudyDto.class);
        String studyImage = studyConfigService.getStudyImage(path);
        studyDto.setStudyImage(studyImage);
        return studyDto;
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm, study);
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
    }

    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }

    public void disableStudyBanner(Study study) {
        study.setUseBanner(false);
    }


    public void publish(Study study) {
        study.publish();
        eventPublisher.publishEvent(new StudyCreatedEvent(study));
    }

    public void close(Study study) {
        study.close();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디를 종료했습니다.."));
    }

    public void startRecruit(Study study) {
        study.startRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 시작합니다."));
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 중단합니다."));
    }

    public boolean isValidPath(String newPath) {
        return !studyRepository.existsByPath(newPath);
    }

    public void updateStudyPath(Study study, String newPath) {
        study.setPath(newPath);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void updateStudyTitle(Study study, String newTitle) {
        study.setTitle(newTitle);
    }

    public void remove(Study study) {
        if (study.isRemovable()) {
            studyRepository.delete(study);
        } else {
            throw new IllegalArgumentException("스터디를 삭제할 수 없습니다.");
        }
    }

    public void addMember(Study study, Account account) {
        AccountStudyMembers member = AccountStudyMembers.builder()
                .account(account)
                .study(study)
                .build();
        study.addMember(member);
    }

    public void removeMember(Study study, Account account) {
        study.removeMember(account);
    }

    public Study getStudyToEnroll(String path) {
        Study study = studyRepository.findStudyOnlyByPath(path);
        return study;
    }

    public void generateTestStudies(Account account) {
        for (int i = 0; i < 30; i++) {
            String randomValue = RandomString.make(5);
            Study study = Study.builder()
                    .title("테스트 스터디" + randomValue)
                    .path("test-" + randomValue)
                    .shortDescription("테스트용 스터디입니다...")
                    .fullDescription("테스트용ㅇㅇㅇㅇㅇ")
                    .build();
            Study newStudy = createNewStudy(study, account);
            Tag jpa = tagRepository.findByTitle("JPA").get();
            StudyTag studytag = StudyTag.builder().study(newStudy).tag(jpa).build();
            newStudy.getTags().add(studytag);
        }
    }

    public boolean checkIfJoined(Study study, Account account) {
        return study.getMembers().stream()
                .map(AccountStudyMembers::getAccount)
                .anyMatch(account1 -> account1.getEmail().equals(account.getEmail()));
    }
}
