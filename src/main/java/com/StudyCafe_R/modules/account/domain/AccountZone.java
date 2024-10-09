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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ACCOUNT_ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Zone zone;
}
