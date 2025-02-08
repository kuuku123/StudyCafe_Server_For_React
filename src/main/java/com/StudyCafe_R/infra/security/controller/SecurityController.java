package com.StudyCafe_R.infra.security.controller;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.infra.security.service.SecurityService;
import com.StudyCafe_R.modules.account.dto.OAuth2Dto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/on-oauth-success")
    public String onSocialSuccess(@RequestBody OAuth2Dto oAuth2Dto, HttpServletRequest request, HttpServletResponse response) {
        String url = securityService.chooseOptioncreateAccount(oAuth2Dto, request, response);
        return url;
    }
}
