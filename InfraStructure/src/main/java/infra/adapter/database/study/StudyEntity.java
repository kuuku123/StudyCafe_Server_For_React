package infra.adapter.database.study;


import infra.adapter.database.account.AccountStudyManagerEntity;
import infra.adapter.database.account.AccountStudyMembersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class StudyEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManagerEntity> managers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyMembersEntity> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Column(columnDefinition = "LONGBLOB")
    private byte[] studyImage;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionText;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyTagEntity> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyZoneEntity> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    private int memberCount;

}
