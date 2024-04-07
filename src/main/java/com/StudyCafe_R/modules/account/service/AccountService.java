package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.infra.config.AppProperties;
import com.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.modules.account.AccountRepository;
import com.StudyCafe_R.modules.account.UserAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountTag;
import com.StudyCafe_R.modules.account.domain.AccountZone;
import com.StudyCafe_R.modules.account.form.Notifications;
import com.StudyCafe_R.modules.account.form.Profile;
import com.StudyCafe_R.modules.account.form.SignUpForm;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagRepository;
import com.StudyCafe_R.modules.zone.Zone;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final SecurityContextRepository securityContextRepository;

    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    public Account processNewAccount(SignUpForm signUpForm) {

        Account newAccount = saveNewAccount(signUpForm);
        sendSignupConfirmEmail(newAccount);
        return newAccount;
    }
    private Account saveNewAccount(SignUpForm signUpForm) {

        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public void sendSignupConfirmEmail(Account newAccount) {

        Context context = new Context();
        context.setVariable("link","/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        context.setVariable("nickname",newAccount.getNickname());
        context.setVariable("linkName","이메일 인증하기");
        context.setVariable("message","스터디 카페 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());

        String message = templateEngine.process("email/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .from("tonydevpc123@gmail.com")
                .subject("스터디 카페 , 회원가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    //TODO password Authentication 이 정석적인 방법이 아니라 혼란을 야기할 수 있다.
    //TODO saveContext를 하기위해서  request, response를 controller부터 쭈욱 받아오고 있다.
    public void login(Account account, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(token);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context,request ,response);

    }

    public void completeSignUp(Account account, HttpServletRequest request, HttpServletResponse response) {
        account.completeSignUp();
        login(account, request, response);
    }

    public void updateProfile(Account account, Profile profile) {
        modelMapper.map(profile,account);
        accountRepository.save(account); // merge detached entity
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications,account);
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname, HttpServletRequest request, HttpServletResponse response) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account, request, response);
    }

    public void sendLoginLink(Account account) {

        Context context = new Context();
        context.setVariable("link","/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname",account.getNickname());
        context.setVariable("linkName","스터디 카페 로그인하기");
        context.setVariable("message","로그인 하려면 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());

        String message = templateEngine.process("email/simple-link", context);


        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("스터디 카페 , 로그인 링크")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
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
        if(!exist) {
            repoAccount.addAccountZone(accountZone);
        }
    }

    public void removeZone(Account account, Zone zone) {
        accountRepository.findById(account.getId())
                        .ifPresent(a -> a.removeAccountZone(zone));
    }

    public Account getAccount(String nickname) {
        Account account = accountRepository.findByNickname(nickname);
        if (account == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }
        return account;
    }
}
