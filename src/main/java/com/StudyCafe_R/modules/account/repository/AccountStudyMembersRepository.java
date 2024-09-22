package com.StudyCafe_R.modules.account.repository;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountStudyMembersRepository extends JpaRepository<AccountStudyManager, Long> {

    @Query("select asm.account from AccountStudyMembers asm where asm.account.id = :accountId and asm.study.id = :studyId")
    List<Account>  findStudyMembers(@Param("accountId") Long accountId, @Param("studyId") Long studyId);
}
