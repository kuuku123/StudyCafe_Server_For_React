package infra.adapter.database.study;

import infra.adapter.database.tag.TagEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_tag")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class StudyTagEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "study_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StudyEntity study;

    @ManyToOne
    @JoinColumn(name = "tag_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private TagEntity tag;

}
