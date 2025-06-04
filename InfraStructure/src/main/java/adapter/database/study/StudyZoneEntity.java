package adapter.database.study;

import adapter.database.zone.ZoneEntity;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "study_zone")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class StudyZoneEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StudyEntity study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ZoneEntity zone;
}
