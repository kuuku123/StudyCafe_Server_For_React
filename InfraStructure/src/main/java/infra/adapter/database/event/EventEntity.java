package infra.adapter.database.event;

import infra.adapter.database.account.AccountEntity;
import infra.adapter.database.enrollment.EnrollmentEntity;
import infra.adapter.database.study.StudyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Event.withEnrollments",
        attributeNodes = {
                @NamedAttributeNode("enrollments"),
        }
)
@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class EventEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_event_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StudyEntity study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private Integer limitOfEnrollments;

    //TODO check if casacde if needed
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<EnrollmentEntity> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;


}
