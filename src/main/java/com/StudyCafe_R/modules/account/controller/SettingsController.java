package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.dto.*;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.account.validator.NicknameValidator;
import com.StudyCafe_R.modules.account.validator.PasswordFormValidator;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.TagForm;
import com.StudyCafe_R.modules.tag.TagRepository;
import com.StudyCafe_R.modules.tag.TagService;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.ZoneService;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.StudyCafe_R.modules.zone.dto.ZoneForm;
import com.StudyCafe_R.modules.zone.ZoneRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static com.StudyCafe_R.modules.account.controller.SettingsController.ROOT;
import static com.StudyCafe_R.modules.account.controller.SettingsController.SETTINGS;


@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + SETTINGS)
public class SettingsController {


    static final String ROOT = "/";
    static final String SETTINGS = "settings";
    static final String PROFILE = "/profile";
    static final String PASSWORD = "/password";
    static final String NOTIFICATIONS = "/notifications";
    static final String ACCOUNT = "/account";
    static final String TAGS = "/tags";
    static final String ZONES = "/zones";

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final NicknameValidator nicknameValidator;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;
    private final ZoneService zoneService;


    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(PROFILE)
    public String updateProfileForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));

        return SETTINGS + PROFILE;
    }

    @PostMapping(PROFILE)
    public ResponseEntity<String> updateProfile(@CurrentAccount Account account, @Valid @RequestBody Profile profile) {
        accountService.updateProfile(account, profile);
        ApiResponse<ByteArrayResource> apiResponse = new ApiResponse<>("update complete", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public ResponseEntity<String> updatePassword(@CurrentAccount Account account, @Valid @RequestBody PasswordForm passwordForm, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            ApiResponse<Map<String, String>> passwordUpdateFailed = new ApiResponse<>("password update failed", HttpStatus.BAD_REQUEST, errorMap);
            return new ResponseEntity<>(new Gson().toJson(passwordUpdateFailed), HttpStatus.BAD_REQUEST);
        }

        AccountDto accountDto = accountService.updatePassword(account, passwordForm.getNewPassword());
        ApiResponse<AccountDto> passwordUpdateSucceed = new ApiResponse<>("password update succeed", HttpStatus.OK, accountDto);
        return new ResponseEntity<>(new Gson().toJson(passwordUpdateSucceed), HttpStatus.OK);
    }

    @GetMapping(NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTINGS + NOTIFICATIONS;
    }

    @PostMapping(NOTIFICATIONS)
    public String updateNotifications(@CurrentAccount Account account, @Valid Notifications notifications, Errors errors,
                                      Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/" + SETTINGS + NOTIFICATIONS;
    }


    @GetMapping(ACCOUNT)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT)
    public String updateAccount(@CurrentAccount Account account, @Valid NicknameForm nicknameForm, Errors errors
            , Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameForm.getNickname(), request, response);
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
        return "redirect:/" + SETTINGS + ACCOUNT;
    }

    @GetMapping(TAGS)
    public ResponseEntity<String> getTags(@CurrentAccount Account account) {
        List<TagDto> tagDtos = accountService.getTags(account);
        ApiResponse<List<TagDto>> apiResponse = new ApiResponse<>("get tags complete", HttpStatus.OK, tagDtos);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping(TAGS + "/add")
    @ResponseBody
    public ResponseEntity<String> addTag(@CurrentAccount Account account, Model model, @RequestBody List<TagForm> tagFormList) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (TagForm tagForm : tagFormList) {
            Tag tag = tagService.findOrCreateNew(tagForm.getTitle());
            accountService.addTag(account, tag);
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtos.add(tagDto);
        }
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        accountDto.setTags(tagDtos);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("tag add complete", HttpStatus.OK, accountDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping(TAGS + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm) {
        String title = tagForm.getTitle();
        Optional<Tag> tag = tagRepository.findByTitle(title);
        if (tag.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag.get());
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("tag remove complete", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @GetMapping(ZONES)
    public ResponseEntity<String> getZones(@CurrentAccount Account account) throws JsonProcessingException {
        List<ZoneDto> accountZones = accountService.getZones(account);
        ApiResponse<List<ZoneDto>> apiResponse = new ApiResponse<>("get tags complete", HttpStatus.OK, accountZones);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping(ZONES + "/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentAccount Account account, @RequestBody List<ZoneForm> zoneFormList) {
        List<ZoneDto> zoneDtos = new ArrayList<>();
        for (ZoneForm zoneForm : zoneFormList) {
            Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCity(), zoneForm.getProvince());
            accountService.addZone(account, zone);
            ZoneDto zoneDto = modelMapper.map(zone, ZoneDto.class);
            zoneDtos.add(zoneDto);
        }
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        accountDto.setZones(zoneDtos);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("zone add complete", HttpStatus.OK, accountDto);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping(ZONES + "/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCity(), zoneForm.getProvince());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("zone remove complete", HttpStatus.OK, null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


}
