package com.StudyCafe_R.modules.account.domain;

import com.StudyCafe_R.modules.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AccountTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Tag tag;

}
