package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.infra.security.service.SecurityService;
import com.StudyCafe_R.modules.account.dto.OAuth2Dto;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

    private final SecurityService securityService;

    @GetMapping("/merge-account")
    public ResponseEntity<String> mergeAccounts(@RequestBody OAuth2Dto oAuth2Dto, HttpServletRequest request, HttpServletResponse response) {
        AccountDto accountDto = securityService.mergeAccount(oAuth2Dto, request, response);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("account has merged", HttpStatus.OK, accountDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/separate-account")
    public ResponseEntity<String> separateAccounts(@RequestBody OAuth2Dto oAuth2Dto) {
        System.out.println("oAuth2Dto = " + oAuth2Dto);
        ApiResponse<String> apiResponse = new ApiResponse<>("account has separated", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

}
