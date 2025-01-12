package com.StudyCafe_R.modules.study.controller;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.service.StudyService;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.study.validator.StudyFormValidator;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StudyController {


    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;
    private final StudyRepository studyRepository;

    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }


    @GetMapping("/get-study/{path}")
    public ResponseEntity<String> getStudy(@CurrentAccount Account account, @PathVariable String path) {
        StudyDto studyDto = studyService.getStudyDto(path);
        ApiResponse<StudyDto> apiResponse = new ApiResponse<>("get study succeeded", HttpStatus.OK, studyDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/new-study")
    public ResponseEntity<String> newStudySubmit(@CurrentAccount Account account, @RequestBody @Valid StudyForm studyForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            ApiResponse<Map<String, String>> createStudyFailed = new ApiResponse<>("create Study Failed", HttpStatus.BAD_REQUEST, errorMap);
            return new ResponseEntity<>(new Gson().toJson(createStudyFailed), HttpStatus.BAD_REQUEST);
        }

        Study newStudy = studyService.createNewStudy(studyForm, account);

        ApiResponse<StudyForm> apiResponse = new ApiResponse<>("create study succeeded", HttpStatus.OK, studyForm);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/study/{path}")
    public String viewStudy(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);
        Study study = studyService.getStudy(path);
        model.addAttribute(study);

        return "study/view";
    }

    @GetMapping("/study/{path}/checkJoined")
    public ResponseEntity<String> checkJoinedStudy(@CurrentAccount Account account, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        boolean joined = studyService.checkIfJoined(study, account);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>("study join succeeded", HttpStatus.OK, joined);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


    @PostMapping("/study/{path}/join")
    public ResponseEntity<String> joinStudy(@CurrentAccount Account account, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        studyService.addMember(study, account);
        ApiResponse<String> apiResponse = new ApiResponse<>("study join succeeded", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/study/{path}/leave")
    public ResponseEntity<String> leaveStudy(@CurrentAccount Account account, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        studyService.removeMember(study, account);
        ApiResponse<String> apiResponse = new ApiResponse<>("study leave succeeded", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/study/data")
    public String generateTestData(@CurrentAccount Account account) {
        studyService.generateTestStudies(account);
        return "redirect:/";
    }
}
