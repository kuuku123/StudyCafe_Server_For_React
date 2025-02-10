package com.StudyCafe_R.modules.study.controller;

import com.StudyCafe_R.infra.config.converter.LocalDateTimeAdapter;
import com.StudyCafe_R.infra.util.MyConstants;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.study.service.StudyService;
import com.StudyCafe_R.modules.study.form.StudyDescriptionForm;
import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.study.service.StudyConfigService;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagForm;
import com.StudyCafe_R.modules.tag.TagService;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.StudyCafe_R.modules.zone.dto.ZoneForm;
import com.StudyCafe_R.modules.zone.ZoneRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/study/{path}/settings")
@RequiredArgsConstructor
public class StudySettingController {

    private final StudyService studyService;
    private final StudyConfigService studyConfigService;
    private final ModelMapper modelMapper;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final AccountService accountService;

    @GetMapping("/description")
    public String viewStudySetting(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(modelMapper.map(study, StudyDescriptionForm.class));
        return "study/settings/description";
    }


    @PostMapping("/update-study")
    public ResponseEntity<String> newStudySubmit(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @RequestBody @Valid StudyForm studyForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            ApiResponse<Map<String, String>> createStudyFailed = new ApiResponse<>("update Study Failed", HttpStatus.BAD_REQUEST, errorMap);
            return new ResponseEntity<>(new Gson().toJson(createStudyFailed), HttpStatus.BAD_REQUEST);
        }
        Account account = accountService.getAccount(email);
        Study study = studyConfigService.updateStudyInfo(account, studyForm);

        ApiResponse<StudyForm> apiResponse = new ApiResponse<>("study update succeeded", HttpStatus.OK, studyForm);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping(value = "/study-image")
    public ResponseEntity<String> studyImage(@PathVariable String path) {
        String encodedImage = studyConfigService.getStudyImage(path);
        ApiResponse<String> apiResponse = new ApiResponse<>("study-image", HttpStatus.OK, encodedImage);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


    @GetMapping("/tags")
    public ResponseEntity<String> studyTagsForm(@PathVariable String path) throws JsonProcessingException {
        List<TagDto> studyTagDtoList = studyConfigService.getStudyTags(path);
        ApiResponse<List<TagDto>> apiResponse = new ApiResponse<>("tag added", HttpStatus.OK, studyTagDtoList);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping(value = "/tags/add")
    @ResponseBody
    public ResponseEntity<String> addTag(@PathVariable String path,
                                         @RequestBody List<TagForm> tagFormList) {
        studyConfigService.addTag(path, tagFormList);
        ApiResponse<Object> apiResponse = new ApiResponse<>("tag added", HttpStatus.OK, null);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/tags/remove")
    @ResponseBody
    public ResponseEntity<String> removeTag(@PathVariable String path, @RequestBody TagForm tagForm) {
        studyConfigService.removeTag(path, tagForm);
        ApiResponse<Object> apiResponse = new ApiResponse<>("tag removed", HttpStatus.OK, null);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/zones")
    public ResponseEntity<String> studyZonesForm(@PathVariable String path, Model model) throws JsonProcessingException {
        List<ZoneDto> zoneDtoList = studyConfigService.getStudyZones(path);
        ApiResponse<List<ZoneDto>> apiResponse = new ApiResponse<>("tag added", HttpStatus.OK, zoneDtoList);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/zones/add")
    @ResponseBody
    public ResponseEntity<String> addZone(@PathVariable String path,
                                          @RequestBody List<ZoneForm> zoneFormList) {
        studyConfigService.addZone(path, zoneFormList);
        ApiResponse<Object> apiResponse = new ApiResponse<>("zone added", HttpStatus.OK, null);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/zones/remove")
    @ResponseBody
    public ResponseEntity<String> removeZone(@PathVariable String path,
                                             @RequestBody ZoneForm zoneForm) {
        studyConfigService.removeZone(path, zoneForm);
        ApiResponse<Object> apiResponse = new ApiResponse<>("zone removed", HttpStatus.OK, null);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishStudy(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @PathVariable String path) {
        Account account = accountService.getAccount(email);
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.publish(study);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>("study published", HttpStatus.OK, study.isPublished());
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/study/close")
    public String closeStudy(@CurrentAccount Account account, @PathVariable String path, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.close(study);
        redirectAttributes.addFlashAttribute("message", "스터디를 종료했습니다.");
        return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
    }

    @PostMapping("/recruit/start")
    public String startRecruit(@CurrentAccount Account account, @PathVariable String path, Model model,
                               RedirectAttributes attributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        if (!study.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
        }

        studyService.startRecruit(study);
        attributes.addFlashAttribute("message", "인원 모집을 시작합니다.");
        return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
    }

    @PostMapping("/recruit/stop")
    public String stopRecruit(@CurrentAccount Account account, @PathVariable String path, Model model,
                              RedirectAttributes attributes) {
        Study study = studyService.getStudyToUpdate(account, path);
        if (!study.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
        }

        studyService.stopRecruit(study);
        attributes.addFlashAttribute("message", "인원 모집을 종료합니다.");
        return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
    }

    @PostMapping("/study/path")
    public String updateStudyPath(@CurrentAccount Account account, @PathVariable String path, String newPath,
                                  Model model, RedirectAttributes attributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        if (!studyService.isValidPath(newPath)) {
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute("studyPathError", "해당 스터디 경로는 사용할 수 없습니다. 다른 값을 입력하세요.");
            return "study/settings/study";
        }

        studyService.updateStudyPath(study, newPath);
        attributes.addFlashAttribute("message", "스터디 경로를 수정했습니다.");
        return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
    }

    @PostMapping("/study/title")
    public String updateStudyTitle(@CurrentAccount Account account, @PathVariable String path, String newTitle,
                                   Model model, RedirectAttributes attributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        if (!studyService.isValidTitle(newTitle)) {
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute("studyTitleError", "스터디 이름을 다시 입력하세요.");
            return "study/settings/study";
        }

        studyService.updateStudyTitle(study, newTitle);
        attributes.addFlashAttribute("message", "스터디 이름을 수정했습니다.");
        return "redirect:/study/" + study.getEncodedPath() + "/settings/study";
    }

    @PostMapping("/study/remove")
    public String removeStudy(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.remove(study);
        return "redirect:/";
    }
}
