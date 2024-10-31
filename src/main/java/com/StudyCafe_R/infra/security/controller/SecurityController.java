package com.StudyCafe_R.infra.security.controller;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.infra.security.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping("/on-oauth-success")
    public String onSocialSuccess(@AuthenticationPrincipal PrincipalUser principalUser, HttpServletRequest request, HttpServletResponse response) {
        String url = securityService.chooseOptioncreateAccount(principalUser, request, response);
        return url;
    }
}
