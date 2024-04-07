package com.StudyCafe_R.modules.study.domain;

import com.StudyCafe_R.modules.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_zone")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class StudyZone {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

}