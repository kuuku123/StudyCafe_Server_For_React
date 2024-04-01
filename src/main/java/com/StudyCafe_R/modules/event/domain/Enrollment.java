package com.StudyCafe_R.modules.event.domain;

import com.StudyCafe_R.modules.account.domain.Account;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;
}