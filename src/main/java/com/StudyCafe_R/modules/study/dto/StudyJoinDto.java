package com.StudyCafe_R.modules.study.dto;

import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;

import java.util.List;

public class StudyJoinDto {

    private Long id;
    private String title;
    private String path;
    private String shortDescription;
    private List<TagDto> tagDtoList;
    private List<ZoneDto> zoneDtoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<TagDto> getTagDtoList() {
        return tagDtoList;
    }

    public void setTagDtoList(List<TagDto> tagDtoList) {
        this.tagDtoList = tagDtoList;
    }

    public List<ZoneDto> getZoneDtoList() {
        return zoneDtoList;
    }

    public void setZoneDtoList(List<ZoneDto> zoneDtoList) {
        this.zoneDtoList = zoneDtoList;
    }


}
