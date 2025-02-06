package com.StudyCafe_R.infra.security.service;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
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

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Transactional
    public String chooseOptioncreateAccount(PrincipalUser principalUser, HttpServletRequest request, HttpServletResponse response) {
        Account account = null;
        try {
            account = accountService.getAccount(principalUser.getAttribute("email"));
        } catch (Exception e) {
        }
        if (account != null) {
            String mergedSocialProviders = account.getCreatedOrMergedSocialProviders();
            if (mergedSocialProviders != null) {
                String[] providers = mergedSocialProviders.split(",");
                for (String provider : providers) {
                    if (provider.equals(principalUser.providerUser().getProvider())) {
                        accountService.saveAuthentication(request, response, account, account.getPassword(), true);
                        return "redirect:" + allowedOrigin + "/already-merged-account";
                    }
                }
            }
            return "redirect:" + allowedOrigin + "/merge-account";
        } else {
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setNickname(principalUser.getAttribute("name"));
            signUpRequest.setEmail(principalUser.getAttribute("email"));
            signUpRequest.setPassword(principalUser.getPassword());
            Account createdAccount = accountService.processNewAccount(signUpRequest);
            String createdOrMergedSocialProviders = createdAccount.getCreatedOrMergedSocialProviders();
            createdOrMergedSocialProviders += "," + principalUser.providerUser().getProvider();
            createdAccount.setCreatedOrMergedSocialProviders(createdOrMergedSocialProviders);
            accountService.saveAuthentication(request, response, createdAccount, createdAccount.getPassword(), true);

            return "redirect:" + allowedOrigin + "/social-account-setPassword";
        }
    }

    @Transactional
    public AccountDto mergeAccount(PrincipalUser principalUser, HttpServletRequest request, HttpServletResponse response) {
        String sub = principalUser.getAttribute("sub");
        Account account = accountService.getAccount(principalUser.getAttribute("email"));
        BigInteger subSocialIdentifier = new BigInteger(sub);
        account.setSubSocialIdentifier(subSocialIdentifier);

        String createdOrMergedSocialProviders = account.getCreatedOrMergedSocialProviders();
        createdOrMergedSocialProviders += "," + principalUser.providerUser().getProvider();
        account.setCreatedOrMergedSocialProviders(createdOrMergedSocialProviders);
        account.setEmailVerified(true);

        accountService.saveAuthentication(request, response, account, account.getPassword(), true);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return accountDto;
    }


}
