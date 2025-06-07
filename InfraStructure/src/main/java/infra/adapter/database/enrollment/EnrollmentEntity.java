package infra.adapter.database.enrollment;

import infra.adapter.database.account.AccountEntity;
import infra.adapter.database.event.EventEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class EnrollmentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_event_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

}
