package com.StudyCafe_R.infra.security.service;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.modules.account.dto.OAuth2Dto;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class SecurityService {

    @Value("${front.redirectUrl}")
    private String redirectUrl;

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Transactional
    public String chooseOptioncreateAccount(OAuth2Dto oAuth2Dto, HttpServletRequest request, HttpServletResponse response) {
        Account account = null;
        try {
            account = accountService.getAccount(oAuth2Dto.getAttribute("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (account != null) {
            String mergedSocialProviders = account.getCreatedOrMergedSocialProviders();
            if (mergedSocialProviders != null) {
                String[] providers = mergedSocialProviders.split(",");
                for (String provider : providers) {
                    if (provider.equals(oAuth2Dto.getProvider())){
//                        accountService.saveAuthentication(request, response, account, account.getPassword(), true);
                        return "redirect:" + redirectUrl + "/already-merged-account";
                    }
                }
            }
            return "redirect:" + redirectUrl + "/merge-account";
        } else {
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setNickname(oAuth2Dto.getAttribute("name"));
            signUpRequest.setEmail(oAuth2Dto.getAttribute("email"));
            signUpRequest.setPassword("");
            Account createdAccount = accountService.processNewAccount(signUpRequest);
            String createdOrMergedSocialProviders = createdAccount.getCreatedOrMergedSocialProviders();
            createdOrMergedSocialProviders += "," + oAuth2Dto.getProvider();
            createdAccount.setCreatedOrMergedSocialProviders(createdOrMergedSocialProviders);
//            accountService.saveAuthentication(request, response, createdAccount, createdAccount.getPassword(), true);

            return "redirect:" + redirectUrl + "/social-account-setPassword";
        }
    }

    @Transactional
    public AccountDto mergeAccount(OAuth2Dto oAuth2Dto, HttpServletRequest request, HttpServletResponse response) {
        String sub = oAuth2Dto.getAttribute("sub");
        Account account = accountService.getAccount(oAuth2Dto.getAttribute("email"));
        BigInteger subSocialIdentifier = new BigInteger(sub);
        account.setSubSocialIdentifier(subSocialIdentifier);

        String createdOrMergedSocialProviders = account.getCreatedOrMergedSocialProviders();
        createdOrMergedSocialProviders += "," + oAuth2Dto.getProvider();
        account.setCreatedOrMergedSocialProviders(createdOrMergedSocialProviders);
        account.setEmailVerified(true);

        accountService.saveAuthentication(request, response, account, account.getPassword(), true);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return accountDto;
    }


}
