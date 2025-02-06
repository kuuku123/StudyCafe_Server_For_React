package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.infra.security.JwtUtils;
import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.form.LoginForm;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("xsrf-token")
    public ResponseEntity<String> xsrfToken() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.login(loginForm, request, response);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("login succeed", HttpStatus.OK, accountDto);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(@RequestHeader("X-User-Email") String email) {
        Account account = accountService.getAccount(email);
        AccountDto accountDto = accountService.getAccountDto(account);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CurrentAccount Account account, HttpServletRequest request, HttpServletResponse response) {
        accountService.logout(account, request);
        ApiResponse<String> apiResponse = new ApiResponse<>("logout succeed", HttpStatus.OK, null);
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


    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(email);
        String view = "email/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        accountService.completeSignUp(account, request, response);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "email/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public ResponseEntity<String> resendConfirmEmail(@CurrentAccount Account account, Model model) {
        if (account.canSendConfirmationEmail()) {
            ApiResponse<String> apiResponse = new ApiResponse<>("The retransmission cycle is 1 hour.", HttpStatus.BAD_REQUEST, null);
            return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.BAD_REQUEST);
        }
        account.generateEmailCheckToken();
        accountService.sendSignupConfirmEmail(account);
        ApiResponse<String> apiResponse = new ApiResponse<>("resend succeed.", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentAccount Account account) {
        Account byNickname = accountService.getAccount(nickname);
        model.addAttribute("account", byNickname);
        model.addAttribute("isOwner", byNickname.equals(account));
        return "account/profile";
    }

    @GetMapping("/email-login")
    public String emailLoginForm() {
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendEmailLoginLink(String email, Model model, RedirectAttributes attributes) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }

        if (!account.canSendConfirmationEmail()) {
            model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
            return "account/email-login";
        }

        accountService.sendLoginLink(account);
        attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/logged-in-by-email";
        if (account == null || !account.isValidToken(token)) {
            model.addAttribute("error", "로그인할 수 없습니다.");
            return view;
        }

        //TODO just for compile
        LoginForm loginForm = new LoginForm();
        accountService.login(loginForm, request, response);
        return view;
    }
}
