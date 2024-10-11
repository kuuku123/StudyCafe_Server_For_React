package com.StudyCafe_R.modules.study.controller;

import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.study.dto.PageRequestDto;
import com.StudyCafe_R.modules.study.dto.StudyJoinDto;
import com.StudyCafe_R.modules.study.service.StudyConfigService;
import com.StudyCafe_R.modules.study.service.StudyJoinService;
import com.StudyCafe_R.modules.study.service.StudyService;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagForm;
import com.StudyCafe_R.modules.tag.TagService;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.ZoneService;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.StudyCafe_R.modules.zone.dto.ZoneForm;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyJoinController {

    private final StudyJoinService studyJoinService;
    private final TagService tagService;
    private final ZoneService zoneService;

    @GetMapping("/get-study-by-tags-and-zones")
    public ResponseEntity<String> getStudyByTagsAndZones(List<TagForm> tagFormList, List<ZoneForm> zoneFormList, PageRequestDto pageRequestDto) {
        List<Tag> tagList = new ArrayList<>();
        List<Zone>  zoneList = new ArrayList<>();
        for (TagForm tagForm : tagFormList) {
            tagService.findByTitle(tagForm.getTitle()).ifPresent(tagList::add);
        }
        for (Zone zone : zoneList) {
            Zone byCityAndProvince = zoneService.findByCityAndProvince(zone.getCity(), zone.getProvince());
            zoneList.add(byCityAndProvince);
        }
        List<StudyJoinDto> byStudyTagsAndZones = studyJoinService.getByStudyTagsAndZones(tagList, zoneList, pageRequestDto);
        ApiResponse<List<StudyJoinDto>> apiResponse= new ApiResponse<>("tag added", HttpStatus.OK, byStudyTagsAndZones);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

}
