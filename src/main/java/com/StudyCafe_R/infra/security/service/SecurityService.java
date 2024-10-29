package com.StudyCafe_R.infra.security.service;

import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.form.LoginForm;
import com.StudyCafe_R.modules.account.form.SignUpForm;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.account.validator.SignUpFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AccountService accountService;

    @Transactional
    public String createAccount(PrincipalUser principalUser, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.getAccount(principalUser.getAttribute("email"));
        if (account != null) {
            String mergedSocialProviders = account.getCreatedOrMergedSocialProviders();
            if (mergedSocialProviders != null) {
                String[] providers = mergedSocialProviders.split(",");
                for (String provider : providers) {
                    if (provider.equals(principalUser.providerUser().getProvider())) {
                        return "redirect:http://localhost:3000/already-merged-account";
                    }
                }
            }
            return "redirect:http://localhost:3000/merge-account";
        } else {
            SignUpForm signUpForm = new SignUpForm();
            signUpForm.setNickname(principalUser.getName());
            signUpForm.setEmail(principalUser.getEmail());
            Account createdAccount = accountService.processNewAccount(signUpForm);
            String createdOrMergedSocialProviders = createdAccount.getCreatedOrMergedSocialProviders();
            createdOrMergedSocialProviders += "," + principalUser.providerUser().getProvider();
            createdAccount.setCreatedOrMergedSocialProviders(createdOrMergedSocialProviders);

            return "redirect:http://localhost:3000/social-account-setPassword";
        }
    }
}
