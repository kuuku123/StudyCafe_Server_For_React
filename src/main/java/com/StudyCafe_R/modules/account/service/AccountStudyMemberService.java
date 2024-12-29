package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.repository.AccountStudyMembersRepository;
import com.StudyCafe_R.modules.account.responseDto.AccountDto;
import com.StudyCafe_R.modules.account.responseDto.StudyDto;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountStudyMemberService {

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
        List<Study> studiesByAccountId = accountStudyMembersRepository.findStudiesByAccountId(account.getId());
        List<StudyDto> studyDtoList = new ArrayList<>();
        for (Study study : studiesByAccountId) {
            StudyDto accountStudyManagerDto = mapStudytoStudyDto(study);
            studyDtoList.add(accountStudyManagerDto);
        }
        return studyDtoList;
    }

    private StudyDto mapStudytoStudyDto(Study study) {
        StudyDto studyDto = new StudyDto();
        studyDto.setTitle(study.getTitle());
        studyDto.setPath(study.getPath());
        studyDto.setShortDescription(study.getShortDescription());
        studyDto.setFullDescription(study.getFullDescription());
        studyDto.setPublished(study.isPublished());
        byte[] studyImage = study.getStudyImage();
        String encodedImage = Base64.encodeBase64String(studyImage);
        studyDto.setStudyImage(encodedImage);
        return studyDto;
    }
}
