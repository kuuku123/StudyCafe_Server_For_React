package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.repository.AccountStudyManagerRepository;
import com.StudyCafe_R.modules.account.repository.AccountStudyMembersRepository;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountStudyManagerService {

    private final AccountStudyManagerRepository accountStudyManagerRepository;
    private final AccountStudyMembersRepository accountStudyMembersRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public List<AccountDto> getStudyMembers(Study study) {
        List<Account> studyMembers = accountStudyMembersRepository.findStudyMembers(study.getId());
        List<AccountDto> accountDtoList = new ArrayList<>();
        for (Account studyMember : studyMembers) {
            AccountDto accountDto = new AccountDto();
            modelMapper.map(studyMember, accountDto);
            accountDtoList.add(accountDto);
        }
        return accountDtoList;
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getStudyManagers(Study study) {
        List<Account> studyManagers = study.getManagers().stream().map(AccountStudyManager::getAccount).toList();
        List<AccountDto> accountDtoList = new ArrayList<>();
        for (Account studyManager : studyManagers) {
            AccountDto accountDto = new AccountDto();
            modelMapper.map(studyManager, accountDto);
            String profileImage = accountService.getProfileImage(studyManager);
            accountDto.setProfileImage(profileImage);
            accountDtoList.add(accountDto);
        }
        return accountDtoList;
    }

    @Transactional(readOnly = true)
    public List<StudyDto> getStudyList(Account account) {
        List<Study> studiesByAccountId = accountStudyManagerRepository.findStudiesByAccountId(account.getId());
        List<StudyDto> accountStudyManagerDtoList = new ArrayList<>();
        for (Study study : studiesByAccountId) {
            StudyDto accountStudyManagerDto = mapAccountStudyManagerDto(study);
            accountStudyManagerDtoList.add(accountStudyManagerDto);
        }
        return accountStudyManagerDtoList;
    }

    private StudyDto mapAccountStudyManagerDto(Study study) {
        StudyDto accountStudyManagerDto = new StudyDto();
        accountStudyManagerDto.setTitle(study.getTitle());
        accountStudyManagerDto.setPath(study.getPath());
        accountStudyManagerDto.setShortDescription(study.getShortDescription());
        accountStudyManagerDto.setFullDescription(study.getFullDescription());
        accountStudyManagerDto.setPublished(study.isPublished());
        byte[] studyImage = study.getStudyImage();
        String encodedImage = Base64.encodeBase64String(studyImage);
        accountStudyManagerDto.setStudyImage(encodedImage);
        return accountStudyManagerDto;
    }
}
