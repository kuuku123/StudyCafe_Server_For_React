package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.infra.security.JwtUtils;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountTag;
import com.StudyCafe_R.modules.account.domain.AccountZone;
import com.StudyCafe_R.modules.account.dto.*;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.tag.dto.TagDto;
import com.StudyCafe_R.modules.zone.Zone;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public Account processNewAccount(SignUpRequest signUpRequest) {
        Account newAccount = saveNewAccount(signUpRequest);
        return newAccount;
    }

    private Account saveNewAccount(SignUpRequest signUpRequest) {
        Account account = modelMapper.map(signUpRequest, Account.class);

        ClassPathResource imgFile = new ClassPathResource("static/images/anonymous.JPG");
        try (InputStream inputStream = imgFile.getInputStream()) {
            byte[] anonymousProfileJpg = inputStream.readAllBytes();
            account.setProfileImage(anonymousProfileJpg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accountRepository.save(account);
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

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);
        accountRepository.save(account);
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

}
