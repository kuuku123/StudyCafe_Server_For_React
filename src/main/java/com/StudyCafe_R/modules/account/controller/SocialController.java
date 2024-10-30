package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.google.gson.Gson;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/social")
public class SocialController {

    @GetMapping("/merge-accounts")
    public ResponseEntity<String> mergeAccounts(@AuthenticationPrincipal PrincipalUser principalUser) {
        System.out.println("principalUser = " + principalUser);
        ApiResponse<String> apiResponse = new ApiResponse<>("account has merged", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/separate-accounts")
    public ResponseEntity<String> separateAccounts(@AuthenticationPrincipal PrincipalUser principalUser) {
        System.out.println("principalUser = " + principalUser);
        ApiResponse<String> apiResponse = new ApiResponse<>("account has merged", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("get-email")
    public ResponseEntity<String> getEmail(@AuthenticationPrincipal PrincipalUser principalUser) {
        String email = principalUser.getAttribute("email");
        ApiResponse<String> apiResponse = new ApiResponse<>("account has merged", HttpStatus.OK, email);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);

    }
}
