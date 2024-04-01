package com.StudyCafe_R.modules.event;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.event.domain.Enrollment;
import com.StudyCafe_R.modules.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
