package com.StudyCafe_R.modules.study.service;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.study.event.StudyUpdateEvent;
import com.StudyCafe_R.modules.study.exceptions.NotManagerException;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagForm;
import com.StudyCafe_R.modules.tag.TagService;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.ZoneRepository;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.StudyCafe_R.modules.zone.dto.ZoneForm;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyConfigService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final ZoneRepository zoneRepository;
    private final ApplicationEventPublisher eventPublisher;

    public String getStudyImage(String path) {
        Study study = studyRepository.findByPath(path);
        byte[] studyImage = study.getStudyImage();
        String encodedImage = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(studyImage);
        return encodedImage;
    }


    public void updateStudyImage(Study study, StudyForm studyForm) {
        // Remove the data URL prefix if it exists
        String base64Image = studyForm.getStudyImage();
        if (base64Image != null) {
            if (base64Image.startsWith("data:image/jpeg;base64,")) {
                base64Image = base64Image.substring("data:image/jpeg;base64,".length());
            }
            if (base64Image.startsWith("data:image/png;base64,")) {
                base64Image = base64Image.substring("data:image/png;base64,".length());
            }
            if (base64Image.equals("undefined")) {
                return;
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            study.setStudyImage(imageBytes); // dirty update will happen
        }
    }

    public Study updateStudyInfo(Account account, StudyForm studyForm) {
        Study study = studyRepository.findByPath(studyForm.getPath());
        checkIfManager(account, study);
        modelMapper.map(studyForm, study);
        updateStudyImage(study, studyForm);
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "study updated"));
        return study;
    }

    public List<TagDto> getStudyTags(String path) {
        Study study = studyRepository.findByPath(path);
        List<Tag> tags = study.getTags().stream().map(StudyTag::getTag).collect(Collectors.toList());
        ArrayList<TagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtoList.add(tagDto);
        }
        return tagDtoList;
    }

    public List<ZoneDto> getStudyZones(String path) {
        Study study = studyRepository.findByPath(path);
        List<Zone> zones = study.getZones().stream().map(StudyZone::getZone).collect(Collectors.toList());
        List<ZoneDto> zoneDtoList = new ArrayList<>();
        for (Zone zone : zones) {
            ZoneDto zoneDto = modelMapper.map(zone, ZoneDto.class);
            zoneDtoList.add(zoneDto);
        }
        return zoneDtoList;
    }


    private void addTag(Study study, Tag tag) {
        StudyTag studyTag = StudyTag.builder()
                .study(study)
                .tag(tag).build();
        boolean exist = study.getTags().stream()
                .anyMatch(st -> st.getTag() == tag);
        if (!exist) {
            study.addStudyTag(studyTag);
        }
    }

    public void addTag(String path, List<TagForm> tagFormList) {

        Study study = studyRepository.findByPath(path);
        for (TagForm tagForm : tagFormList) {
            Tag tag = tagService.findOrCreateNew(tagForm.getTitle());
            addTag(study, tag);
        }

    }

    public void removeTag(String path, TagForm tagForm) {
        Optional<Tag> tag = tagService.findByTitle(tagForm.getTitle());
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("tag doesn't exist");
        }
        Study study = studyRepository.findByPath(path);
        study.removeStudyTag(tag.get());
    }

    private void addZone(Study study, Zone zone) {
        StudyZone studyZone = StudyZone.builder()
                .zone(zone)
                .study(study)
                .build();
        boolean exist = study.getZones().stream()
                .anyMatch(sz -> sz.getZone() == zone);
        if (!exist) {
            study.addStudyZone(studyZone);
        }
    }

    public void addZone(String path, List<ZoneForm> zoneFormList) {
        Study study = studyRepository.findByPath(path);
        for (ZoneForm zoneForm : zoneFormList) {
            Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCity(), zoneForm.getProvince());
            if (zone == null) {
                throw new IllegalArgumentException("zone doesn't exist");
            }
            addZone(study, zone);
        }
    }

    public void removeZone(String path, ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCity(), zoneForm.getProvince());
        if (zone == null) {
            throw new IllegalArgumentException("zone doesn't exist");
        }
        Study study = studyRepository.findByPath(path);
        study.removeStudyZone(zone);
        studyRepository.save(study);
    }


    public Study getStudyToUpdateTag(Account account, String path) {
        Study study = studyRepository.findStudyWithTagsByPath(path);
        checkIfManager(account, study);
        return study;
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        Study study = studyRepository.findStudyWithZonesByPath(path);
        checkIfManager(account, study);
        return study;
    }


    private void checkIfManager(Account account, Study study) {
        if (!study.isManagedby(account)) {
            throw new NotManagerException("해당 기능을 사용할 수 없습니다.");
        }
    }
}
