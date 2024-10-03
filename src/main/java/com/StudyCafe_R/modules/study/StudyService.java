package com.StudyCafe_R.modules.study;

import com.StudyCafe_R.modules.study.form.StudyDescriptionForm;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.domain.AccountStudyMembers;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.modules.study.event.StudyCreatedEvent;
import com.StudyCafe_R.modules.study.event.StudyUpdateEvent;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagRepository;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final TagRepository tagRepository;

    public Study createNewStudy(Study study, Account account) {
        byte[] anonymousProfileJpg = readFileToByteArray("src/main/resources/images/anonymous.JPG");
        study.setStudyImage(anonymousProfileJpg);
        Study newStudy = studyRepository.save(study);

        AccountStudyManager accountStudyManager = AccountStudyManager.builder()
                .account(account)
                .study(study)
                .build();

        newStudy.addManager(accountStudyManager);
        return newStudy;
    }

    private byte[] readFileToByteArray(String filePath) {
        File file = new File(filePath);
        byte[] byteArray = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public Study getStudyToUpdate(Account account, String path) {
        Study study = getStudy(path);
        checkIfManager(account,study);
        return study;
    }


    public Study getStudy(String path) {
        Study study = studyRepository.findByPath(path);
        return study;
    }

    public List<TagDto> getStudyTags(String path) {
        Study study = getStudy(path);
        List<Tag> tags = study.getTags().stream().map(StudyTag::getTag).collect(Collectors.toList());
        ArrayList<TagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtoList.add(tagDto);
        }
        return tagDtoList;
    }

    public List<ZoneDto> getStudyZones(String path) {
        Study study = getStudy(path);
        List<Zone> zones = study.getZones().stream().map(StudyZone::getZone).collect(Collectors.toList());
        List<ZoneDto> zoneDtoList = new ArrayList<>();
        for (Zone zone : zones) {
            ZoneDto zoneDto = modelMapper.map(zone, ZoneDto.class);
            zoneDtoList.add(zoneDto);
        }
        return zoneDtoList;
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm,study);
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"스터디 소개를 수정했습니다."));
    }

    public void updateStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }

    public void disableStudyBanner(Study study) {
        study.setUseBanner(false);
    }

    public void addTag(Study study, Tag tag) {
        StudyTag studyTag = StudyTag.builder()
                .study(study)
                .tag(tag).build();
        Study repoStudy = studyRepository.findByPath(study.getPath());
        boolean exist = repoStudy.getTags().stream()
                .anyMatch(st -> st.getTag() == tag);
        if(!exist) {
            repoStudy.addStudyTag(studyTag);
        }
    }

    public void removeTag(Study study, Tag tag) {
        Study repoStudy = studyRepository.findByPath(study.getPath());
        repoStudy.removeStudyTag(tag);
    }

    public void addZone(Study study, Zone zone) {
        StudyZone studyZone = StudyZone.builder()
                .zone(zone)
                .study(study)
                .build();
        Study repoStudy = studyRepository.findByPath(study.getPath());
        boolean exist = repoStudy.getZones().stream()
                .anyMatch(sz -> sz.getZone() == zone);
        if(!exist) {
            repoStudy.addStudyZone(studyZone);
        }
    }

    public void removeZone(Study study,Zone zone) {
        Study repoStudy = studyRepository.findByPath(study.getPath());
        repoStudy.removeStudyZone(zone);
        studyRepository.save(study);
    }

    public Study getStudyToUpdateTag(Account account, String path) {
        Study study = studyRepository.findStudyWithTagsByPath(path);
        checkIfManager(account,study);
        return study;
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        Study study = studyRepository.findStudyWithZonesByPath(path);
        checkIfManager(account,study);
        return study;
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        Study study = studyRepository.findStudyWithManagersByPath(path);
        checkIfManager(account, study);
        return study;
    }

    private void checkIfManager(Account account, Study study) {
        if (!study.isManagedby(account)){
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }


    public void publish(Study study) {
        study.publish();
        eventPublisher.publishEvent(new StudyCreatedEvent(study));
    }

    public void close(Study study) {
        study.close();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"스터디를 종료했습니다.."));
    }

    public void startRecruit(Study study) {
        study.startRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"팀원 모집을 시작합니다."));
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"팀원 모집을 중단합니다."));
    }

    public boolean isValidPath(String newPath) {
        if (!newPath.matches(StudyForm.VALID_PATH_PATTERN)) {
            return false;
        }

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
        for (int i = 0; i< 30; i++) {
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
}
