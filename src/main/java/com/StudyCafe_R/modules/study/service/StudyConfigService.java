package com.StudyCafe_R.modules.study.service;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyConfigService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;


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


    public void addTag(Study study, Tag tag) {
        StudyTag studyTag = StudyTag.builder()
                .study(study)
                .tag(tag).build();
        Study repoStudy = studyRepository.findByPath(study.getPath());
        boolean exist = repoStudy.getTags().stream()
                .anyMatch(st -> st.getTag() == tag);
        if (!exist) {
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
        if (!exist) {
            repoStudy.addStudyZone(studyZone);
        }
    }

    public void removeZone(Study study, Zone zone) {
        Study repoStudy = studyRepository.findByPath(study.getPath());
        repoStudy.removeStudyZone(zone);
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
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }
}
