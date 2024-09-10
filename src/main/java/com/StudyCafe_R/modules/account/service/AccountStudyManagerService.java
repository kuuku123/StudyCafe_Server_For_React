package com.StudyCafe_R.modules.account.service;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.repository.AccountStudyManagerRepository;
import com.StudyCafe_R.modules.account.responseDto.AccountStudyManagerDto;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<AccountStudyManagerDto> getStudyList(Account account) {
        List<Study> studiesByAccountId = accountStudyManagerRepository.findStudiesByAccountId(account.getId());
        List<AccountStudyManagerDto> accountStudyManagerDtoList = new ArrayList<>();
        for (Study study : studiesByAccountId) {
            AccountStudyManagerDto map = modelMapper.map(study, AccountStudyManagerDto.class);
            accountStudyManagerDtoList.add(map);
        }
        return accountStudyManagerDtoList;
    }
}
