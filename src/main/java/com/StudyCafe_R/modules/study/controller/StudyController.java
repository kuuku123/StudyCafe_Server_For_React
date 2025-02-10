package com.StudyCafe_R.modules.study.controller;

import com.StudyCafe_R.infra.util.MyConstants;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.study.repository.StudyRepository;
import com.StudyCafe_R.modules.study.service.StudyService;
import com.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.modules.study.validator.StudyFormValidator;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final AccountService accountService;

    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }


    @GetMapping("/get-study/{path}")
    public ResponseEntity<String> getStudy(@PathVariable String path) {
        StudyDto studyDto = studyService.getStudyDto(path);
        ApiResponse<StudyDto> apiResponse = new ApiResponse<>("get study succeeded", HttpStatus.OK, studyDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/new-study")
    public ResponseEntity<String> newStudySubmit(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @RequestBody @Valid StudyForm studyForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            ApiResponse<Map<String, String>> createStudyFailed = new ApiResponse<>("create Study Failed", HttpStatus.BAD_REQUEST, errorMap);
            return new ResponseEntity<>(new Gson().toJson(createStudyFailed), HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.getAccount(email);
        Study newStudy = studyService.createNewStudy(studyForm, account);

        ApiResponse<StudyForm> apiResponse = new ApiResponse<>("create study succeeded", HttpStatus.OK, studyForm);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/study/{path}/checkJoined")
    public ResponseEntity<String> checkJoinedStudy(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        Account account = accountService.getAccount(email);
        boolean joined = studyService.checkIfJoined(study, account);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>("study join succeeded", HttpStatus.OK, joined);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


    @PostMapping("/study/{path}/join")
    public ResponseEntity<String> joinStudy(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        Account account = accountService.getAccount(email);
        studyService.addMember(study, account);
        ApiResponse<String> apiResponse = new ApiResponse<>("study join succeeded", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/study/{path}/leave")
    public ResponseEntity<String> leaveStudy(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email, @PathVariable String path) {
        Study study = studyRepository.findStudyWithMembersByPath(path);
        Account account = accountService.getAccount(email);
        studyService.removeMember(study, account);
        ApiResponse<String> apiResponse = new ApiResponse<>("study leave succeeded", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/study/data")
    public String generateTestData(Account account) {
        studyService.generateTestStudies(account);
        return "redirect:/";
    }
}
