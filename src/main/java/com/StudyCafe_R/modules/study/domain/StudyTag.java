package com.StudyCafe_R.modules.study.domain;

import com.StudyCafe_R.modules.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_tag")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class StudyTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

}