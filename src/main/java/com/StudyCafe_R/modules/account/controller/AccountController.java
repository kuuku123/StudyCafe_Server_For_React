package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.infra.util.MyConstants;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.account.validator.SignUpFormValidator;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }


    @GetMapping("/profile")
    public ResponseEntity<String> profile(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email) {
        Account account = accountService.getAccount(email);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<String> otherProfile(@PathVariable("email") String email) {
        Account account = accountService.getAccount(email);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUpSubmit(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.processNewAccount(signUpRequest);
        AccountDto accountDto = accountService.getAccountDto(account);

        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("sign up succeed", HttpStatus.OK, accountDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


//
//    @GetMapping("/check-email")
//    public String checkEmail(@CurrentAccount Account account, Model model) {
//        model.addAttribute("email", account.getEmail());
//        return "email/check-email";
//    }
//

}
