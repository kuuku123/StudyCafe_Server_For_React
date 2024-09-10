package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.AccountStudyManagerDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountStudyManagerService;
import com.StudyCafe_R.modules.study.domain.Study;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountStudyManagerController {

    private final AccountStudyManagerService accountStudyManagerService;

    @GetMapping("/studyList")
    public ResponseEntity<String> getStudyList(@CurrentAccount Account account) {
        List<AccountStudyManagerDto> studyList = accountStudyManagerService.getStudyList(account);
        ApiResponse<List<AccountStudyManagerDto>> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, studyList);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }
}
