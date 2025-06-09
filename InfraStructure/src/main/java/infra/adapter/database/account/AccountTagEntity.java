package infra.adapter.database.account;


import com.StudyCafe_R.domain.Tag;
import infra.adapter.database.tag.TagEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private TagEntity tag;

}
