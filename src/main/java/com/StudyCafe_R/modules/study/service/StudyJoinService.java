package com.StudyCafe_R.modules.study.service;

import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.dto.PageRequestDto;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.dto.StudyJoinDto;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;

    public List<StudyJoinDto> getByStudyTagsAndZones(List<Tag> tags, List<Zone> zones, PageRequestDto pageRequestDto) {
        PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("id").descending());
        Page<Study> studyByZoneAndTag = studyRepository.findStudyByZonesAndTags(tags, zones, pageRequest);
        List<StudyJoinDto> studyJoinDtoList = studyByZoneAndTag.map((element) -> modelMapper.map(element, StudyJoinDto.class)).toList();
        return studyJoinDtoList;
    }

}
