package com.StudyCafe_R.modules.account.repository;


import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountStudyManagerRepository extends JpaRepository<AccountStudyManager, Long> {

    @Query("SELECT asm.study FROM AccountStudyManager asm WHERE asm.account.id = :accountId")
    List<Study> findStudiesByAccountId(@Param("accountId") Long accountId);
}
