package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.infra.config.AppProperties;
import com.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.infra.security.PrincipalUser;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.UserAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountTag;
import com.StudyCafe_R.modules.account.domain.AccountZone;
import com.StudyCafe_R.modules.account.form.*;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagRepository;
import com.StudyCafe_R.modules.zone.Zone;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    private final RememberMeServices rememberMeServices;
    private final PersistentTokenRepository persistentTokenRepository;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;
    private final SecurityContextRepository securityContextRepository;

    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostConstruct
    public void init() {
        authenticationProvider.setUserDetailsService(userDetailsService);
    }

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignupConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {

        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();

        ClassPathResource imgFile = new ClassPathResource("static/images/anonymous.JPG");
        try (InputStream inputStream = imgFile.getInputStream()) {
            byte[] anonymousProfileJpg = inputStream.readAllBytes();
            account.setProfileImage(anonymousProfileJpg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accountRepository.save(account);
    }

    public void sendSignupConfirmEmail(Account newAccount) {

        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "Email Verification");
        context.setVariable("message", "Click the link to use the Study Cafe service.");
        context.setVariable("host", appProperties.getHost());

        executorService.submit(() -> {
            String message = templateEngine.process("email/simple-link", context);

            EmailMessage emailMessage = EmailMessage.builder()
                    .to(newAccount.getEmail())
                    .from("tonydevpc123@gmail.com")
                    .subject("Study Cafe , SignUp Verification")
                    .message(message)
                    .build();
            emailService.sendEmail(emailMessage);
        });

    }

    // Storing the Authentication manually
    public Account login(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        String nicknameOrEmail = loginForm.getNicknameOrEmail();
        Account account = getAccount(nicknameOrEmail);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), loginForm.getPassword());
        authenticationProvider.authenticate(token);

        saveAuthentication(request, response, account, loginForm.getPassword());

        return account;
    }


    public void logout(Account account, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        persistentTokenRepository.removeUserTokens(account.getNickname());
    }

    private void saveAuthentication(HttpServletRequest request, HttpServletResponse response, Account account, String password) {
        UsernamePasswordAuthenticationToken authorizedToken = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), passwordEncoder.encode(password), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authorizedToken);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        rememberMeServices.loginSuccess(request, response, authorizedToken);
    }

    public void signUp(Account account, HttpServletRequest request, HttpServletResponse response) {
        saveAuthentication(request, response, account, account.getPassword());
    }

    public void completeSignUp(Account account, HttpServletRequest request, HttpServletResponse response) {
        account.completeSignUp();
        signUp(account, request, response);
    }

    public String getProfileImage(Account account) {
        Account byNickname = accountRepository.findByNickname(account.getNickname());
        byte[] profileImage = byNickname.getProfileImage();
        String encodedImage = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(profileImage);
        return encodedImage;
    }


    public void updateProfile(Account account, Profile profile) {
        // Remove the data URL prefix if it exists
        String base64Image = profile.getProfileImage();
        if (base64Image.startsWith("data:image/jpeg;base64,")) {
            base64Image = base64Image.substring("data:image/jpeg;base64,".length());
        }
        if (base64Image.startsWith("data:image/png;base64,")) {
            base64Image = base64Image.substring("data:image/png;base64,".length());
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        account.setProfileImage(imageBytes);
        modelMapper.map(profile, account);
        accountRepository.save(account); // merge detached entity
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname, HttpServletRequest request, HttpServletResponse response) {
        account.setNickname(nickname);
        accountRepository.save(account);

        //TODO just for compile
        LoginForm loginForm = new LoginForm();
        login(loginForm, request, response);
    }

    public void sendLoginLink(Account account) {

        Context context = new Context();
        context.setVariable("link", "/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "스터디 카페 로그인하기");
        context.setVariable("message", "로그인 하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        executorService.submit(() -> {
            String message = templateEngine.process("email/simple-link", context);
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(account.getEmail())
                    .subject("스터디 카페 , 로그인 링크")
                    .message(message)
                    .build();
            emailService.sendEmail(emailMessage);
        });
    }

    public void addTag(Account account, Tag tag) {
        AccountTag accountTag = AccountTag.builder()
                .tag(tag)
                .build();
        Account repoAccount = accountRepository.findById(account.getId()).get();

        boolean exist = repoAccount.getAccountTagSet().stream()
                .anyMatch(at -> at.getTag() == tag);
        if (!exist) {
            repoAccount.addAccountTag(accountTag);
        }
    }

    public Set<AccountTag> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getAccountTagSet();
    }

    public void removeTag(Account account, Tag tag) {

        Account repoAccount = accountRepository.findById(account.getId()).get();
        repoAccount.removeAccountTag(tag);
//        tagRepository.delete(tag);  // we want tag to be alive , later we can look AccountTag table and search for Tag that has no reference
    }


    public Set<AccountZone> getZones(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getAccountZoneSet();
    }

    public void addZone(Account account, Zone zone) {

        AccountZone accountZone = AccountZone.builder().zone(zone).build();
        Account repoAccount = accountRepository.findById(account.getId()).get();

        boolean exist = repoAccount.getAccountZoneSet().stream()
                .anyMatch(az -> az.getZone() == zone);
        if (!exist) {
            repoAccount.addAccountZone(accountZone);
        }
    }

    public void removeZone(Account account, Zone zone) {
        accountRepository.findById(account.getId())
                .ifPresent(a -> a.removeAccountZone(zone));
    }

    public Account getAccount(String nicknameOrEmail) {
        Account account = accountRepository.findByNickname(nicknameOrEmail);
        if (account == null) {
            account = accountRepository.findByEmail(nicknameOrEmail);
            if (account == null) {
                throw new IllegalArgumentException(nicknameOrEmail + "에 해당하는 사용자가 없습니다.");
            }
        }
        return account;
    }

    public AccountDto getAccountDto(Account account) {
        Account accountFromDb = getAccount(account.getEmail()); // due to session split because of email verfication is localhost:8081 , (2 session get created 3000, 8081 and security are saved in session differently
        AccountDto accountDto = new AccountDto();
        modelMapper.map(accountFromDb, accountDto);
        return accountDto;
    }
}
