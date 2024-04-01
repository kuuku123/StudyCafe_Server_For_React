package com.StudyCafe_R.modules.tag;

import com.StudyCafe_R.modules.account.domain.AccountTag;
import com.StudyCafe_R.modules.study.domain.StudyTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountTag> accountTagSet = new HashSet<>();

    @OneToMany(mappedBy = "tag" , cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyTag> studyTagSet = new HashSet<>();

}
