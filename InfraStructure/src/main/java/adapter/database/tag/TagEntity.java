package adapter.database.tag;


import adapter.database.account.AccountTagEntity;
import adapter.database.study.StudyTagEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<AccountTagEntity> accountTagSet = new HashSet<>();

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<StudyTagEntity> studyTagSet = new HashSet<>();


}
