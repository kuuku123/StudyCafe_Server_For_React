package com.StudyCafe_R.modules.account.domain;

import com.StudyCafe_R.modules.study.domain.Study;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStudyMembers {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}