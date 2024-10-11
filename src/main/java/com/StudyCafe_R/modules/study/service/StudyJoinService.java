package com.StudyCafe_R.modules.study.service;

import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.modules.study.dto.PageRequestDto;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.dto.StudyJoinDto;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

    private final StudyRepository studyRepository;

    public List<StudyJoinDto> getByStudyTagsAndZones(List<Tag> tags, List<Zone> zones, PageRequestDto pageRequestDto) {
        PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("id").descending());
        Page<Study> studyByZoneAndTag = studyRepository.findStudyByZonesAndTags(tags, zones, pageRequest);
        return studyByZoneAndTag.map(this::mapStudyToStudyJoinDto).toList();
    }

    private StudyJoinDto mapStudyToStudyJoinDto(Study study) {

        ArrayList<TagDto> tagDtoList = new ArrayList<>();
        ArrayList<ZoneDto> zoneDtoList = new ArrayList<>();
        Set<StudyTag> tags = study.getTags();
        for (StudyTag tag : tags) {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getTag().getId());
            tagDto.setTitle(tag.getTag().getTitle());
            tagDtoList.add(tagDto);
        }

        Set<StudyZone> zones = study.getZones();
        for (StudyZone zone : zones) {
            ZoneDto zoneDto = new ZoneDto();
            zoneDto.setId(zone.getZone().getId());
            zoneDto.setCity(zone.getZone().getCity());
            zoneDto.setProvince(zone.getZone().getProvince());
            zoneDtoList.add(zoneDto);
        }
        StudyJoinDto studyJoinDto = new StudyJoinDto();
        studyJoinDto.setId(study.getId());
        studyJoinDto.setTagDtoList(tagDtoList);
        studyJoinDto.setZoneDtoList(zoneDtoList);

        return studyJoinDto;
    }

}
