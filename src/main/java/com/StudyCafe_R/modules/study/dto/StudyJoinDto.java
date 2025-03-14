package com.StudyCafe_R.modules.study.dto;

import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StudyJoinDto {

    private Long id;
    private String title;
    private String path;
    private String shortDescription;
    private String fullDescription;
    private List<TagDto> tagDtoList;
    private List<ZoneDto> zoneDtoList;
    private String studyImage;
    private boolean published;
}
