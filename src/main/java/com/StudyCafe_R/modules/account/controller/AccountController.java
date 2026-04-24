package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.infra.util.MyConstants;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.account.validator.SignUpFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    @InitBinder("signUpRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }


    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AccountDto>> profile(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email) {
        Account account = accountService.getAccount(email);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<ApiResponse<AccountDto>> otherProfile(@PathVariable("email") String email) {
        Account account = accountService.getAccount(email);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<?>> signUpSubmit(@RequestBody @jakarta.validation.Valid SignUpRequest signUpRequest, org.springframework.validation.Errors errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            ApiResponse<SignUpRequest> apiResponse = new ApiResponse<>("signup failed", HttpStatus.BAD_REQUEST, signUpRequest);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        Account account = accountService.processNewAccount(signUpRequest);
        AccountDto accountDto = accountService.getAccountDto(account);

        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("sign up succeed", HttpStatus.OK, accountDto);
        return ResponseEntity.ok(apiResponse);
    }
}
