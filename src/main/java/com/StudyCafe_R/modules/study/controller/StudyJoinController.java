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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyJoinController {

    private final StudyJoinService studyJoinService;
    private final TagService tagService;
    private final ZoneService zoneService;

    @GetMapping("/get-study-by-tags-and-zones")
    public ResponseEntity<String> getStudyByTagsAndZones(
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> provinces,
            @RequestParam int page,
            @RequestParam int size) {

        Result result = getResult(tags, cities, provinces, page, size);
        List<StudyJoinDto> byStudyTagsAndZones = studyJoinService.getByStudyTagsAndZones(result.tagList(), result.zoneList(), result.pageRequestDto());
        ApiResponse<List<StudyJoinDto>> apiResponse = new ApiResponse<>("tag added", HttpStatus.OK, byStudyTagsAndZones);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    private Result getResult(List<String> tags, List<String> cities, List<String> provinces, int page, int size) {
        List<TagForm> tagFormList = new ArrayList<>();
        List<ZoneForm> zoneFormList = new ArrayList<>();

        if (tags == null) {
            tags = new ArrayList<>();
        }
        if (cities == null) {
            cities = new ArrayList<>();
        }
        if (provinces == null) {
            provinces = new ArrayList<>();
        }

        for (String tag : tags) {
            TagForm tagForm = new TagForm();
            tagForm.setTitle(tag);
            tagFormList.add(tagForm);
        }

        for (String city : cities) {
            for (String province : provinces) {
                ZoneForm zoneForm = new ZoneForm();
                zoneForm.setCity(city);
                zoneForm.setProvince(province);
                zoneFormList.add(zoneForm);
            }
        }

        PageRequestDto pageRequestDto = new PageRequestDto();
        pageRequestDto.setPage(page);
        pageRequestDto.setSize(size);

        List<Tag> tagList = new ArrayList<>();
        List<Zone> zoneList = new ArrayList<>();
        for (TagForm tagForm : tagFormList) {
            tagService.findByTitle(tagForm.getTitle()).ifPresent(tagList::add);
        }
        for (ZoneForm zoneForm : zoneFormList) {
            Zone byCityAndProvince = zoneService.findByCityAndProvince(zoneForm.getCity(), zoneForm.getProvince());
            zoneList.add(byCityAndProvince);
        }
        Result result = new Result(pageRequestDto, tagList, zoneList);
        return result;
    }

    private record Result(PageRequestDto pageRequestDto, List<Tag> tagList, List<Zone> zoneList) {
    }

}
