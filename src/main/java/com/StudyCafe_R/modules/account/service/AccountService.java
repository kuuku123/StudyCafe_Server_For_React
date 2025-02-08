package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.infra.config.AppProperties;
import com.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.infra.security.JwtUtils;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.UserAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountTag;
import com.StudyCafe_R.modules.account.domain.AccountZone;
import com.StudyCafe_R.modules.account.dto.*;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagRepository;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import io.jsonwebtoken.Claims;
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
import java.util.*;
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
    private final JwtUtils jwtUtils;

    @PostConstruct
    public void init() {
        authenticationProvider.setUserDetailsService(userDetailsService);
    }

    public Account processNewAccount(SignUpRequest signUpRequest) {
        Account newAccount = saveNewAccount(signUpRequest);
        sendSignupConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpRequest signUpRequest) {
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Account account = modelMapper.map(signUpRequest, Account.class);
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

        saveAuthentication(request, response, account, loginForm.getPassword(), false);

        return account;
    }


    public void logout(Account account, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        persistentTokenRepository.removeUserTokens(account.getNickname());
    }

    public void saveAuthentication(HttpServletRequest request, HttpServletResponse response, Account account, String password, boolean alreadyEncoded) {
        String encodedPassword = null;
        if (alreadyEncoded) {
            encodedPassword = password;
        } else {
            encodedPassword = passwordEncoder.encode(password);
        }
        UsernamePasswordAuthenticationToken authorizedToken = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), encodedPassword, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authorizedToken);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        rememberMeServices.loginSuccess(request, response, authorizedToken);
    }

    public void signUp(Account account, HttpServletRequest request, HttpServletResponse response) {
        saveAuthentication(request, response, account, account.getPassword(), false);
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
        if (base64Image != null) {
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
    }

    public AccountDto updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        Account updatedAccount = accountRepository.save(account);
        return modelMapper.map(updatedAccount, AccountDto.class);
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

    public List<TagDto> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        Set<AccountTag> accountTagSet = byId.orElseThrow().getAccountTagSet();
        List<Tag> tags = accountTagSet.stream().map(AccountTag::getTag).toList();
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }

    public void removeTag(Account account, Tag tag) {

        Account repoAccount = accountRepository.findById(account.getId()).get();
        repoAccount.removeAccountTag(tag);
    }


    public List<ZoneDto> getZones(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        Set<AccountZone> accountZoneSet = byId.orElseThrow().getAccountZoneSet();
        List<Zone> zones = accountZoneSet.stream().map(AccountZone::getZone).toList();
        List<ZoneDto> zoneDtos = new ArrayList<>();
        for (Zone zone : zones) {
            ZoneDto zoneDto = modelMapper.map(zone, ZoneDto.class);
            zoneDtos.add(zoneDto);
        }
        return zoneDtos;
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

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public Account getAccount(String nicknameOrEmail) {
        Account account = accountRepository.findByNickname(nicknameOrEmail);
        if (account == null) {
            account = accountRepository.findByEmail(nicknameOrEmail);
            if (account == null) {
                log.error(nicknameOrEmail + "에 해당하는 사용자가 없습니다.");
                throw new IllegalArgumentException(nicknameOrEmail + "에 해당하는 사용자가 없습니다.");
            }
        }
        return account;
    }

    public AccountDto getAccountDto(Account account) {
        Account accountFromDb = getAccount(account.getEmail()); // due to session split because of email verfication is localhost:8081 , (2 session get created 3000, 8081 and security are saved in session differently
        AccountDto accountDto = new AccountDto();
        modelMapper.map(accountFromDb, accountDto);
        List<Tag> tags = accountFromDb.getAccountTagSet().stream().map(AccountTag::getTag).toList();
        List<Zone> zones = accountFromDb.getAccountZoneSet().stream().map(AccountZone::getZone).toList();
        List<TagDto> tagDtos = new ArrayList<>();
        List<ZoneDto> zoneDtos = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtos.add(tagDto);
        }
        for (Zone zone : zones) {
            ZoneDto zoneDto = modelMapper.map(zone, ZoneDto.class);
            zoneDtos.add(zoneDto);
        }
        accountDto.setTags(tagDtos);
        accountDto.setZones(zoneDtos);

        String profileImage = getProfileImage(account);
        accountDto.setProfileImage(profileImage);

        return accountDto;
    }

    public Account getAccountFromAuthHeader(String authHeader) {
        String jwt = jwtUtils.getJWT(authHeader);
        Claims claims = jwtUtils.parseClaims(jwt);
        String email = (String)claims.get("email");

        Account account = getAccount(email);
        return account;
    }
}
