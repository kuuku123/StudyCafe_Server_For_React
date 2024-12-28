package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountStudyManagerService;
import com.StudyCafe_R.modules.study.service.StudyService;
import com.StudyCafe_R.modules.study.domain.Study;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountStudyManagerController {

    private final AccountStudyManagerService accountStudyManagerService;
    private final StudyService studyService;

    @GetMapping("/study-list")
    public ResponseEntity<String> getStudyList(@CurrentAccount Account account) {
        List<StudyDto> studyList = accountStudyManagerService.getStudyList(account);
        ApiResponse<List<StudyDto>> apiResponse = new ApiResponse<>("studyList", HttpStatus.OK, studyList);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/{path}/study-members")
    public ResponseEntity<String> getStudyMembers(@PathVariable String path) {
        Study study = studyService.getStudy(path);
        List<AccountDto> studyMembers = accountStudyManagerService.getStudyMembers(study);
        ApiResponse<List<AccountDto>> apiResponse = new ApiResponse<>("studyMembers", HttpStatus.OK, studyMembers);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/{path}/study-managers")
    public ResponseEntity<String> getStudyManager(@CurrentAccount Account account, @PathVariable String path) {
        Study study = studyService.getStudy(path);
        List<AccountDto> studyManagers = accountStudyManagerService.getStudyManagers(study);
        ApiResponse<List<AccountDto>> apiResponse = new ApiResponse<>("studyMembers", HttpStatus.OK, studyManagers);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }
}
