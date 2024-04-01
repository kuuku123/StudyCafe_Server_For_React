package com.StudyCafe_R.modules.account.domain;

import com.StudyCafe_R.modules.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountZone {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;
}
