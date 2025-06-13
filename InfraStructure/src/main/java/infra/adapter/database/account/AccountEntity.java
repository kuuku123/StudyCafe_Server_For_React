package infra.adapter.database.account;

import infra.adapter.database.enrollment.EnrollmentEntity;
import infra.adapter.database.event.EventEntity;
import infra.adapter.database.tag.TagEntity;
import infra.adapter.database.zone.ZoneEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    //client's extra info

    private String bio;

    private String url;

    private String occupation;

    private String location; // varchar(255) above all info

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    private boolean studyCreatedByEmail;

    @Builder.Default
    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    @Builder.Default
    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    @Builder.Default
    private boolean studyUpdatedByWeb = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AccountTagEntity> accountTagSet = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AccountZoneEntity> accountZoneSet = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManagerEntity> managers = new HashSet<>();


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManagerEntity> members = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<EventEntity> events = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<EnrollmentEntity> enrollments = new HashSet<>();

    public void addAccountTagEntity(AccountTagEntity accountTagEntity) {
        this.accountTagSet.add(accountTagEntity);
        accountTagEntity.syncAccount(this);
    }

    public void removeAccountTagEntity(TagEntity tagEntity) {
        accountTagSet.removeIf(accountTag -> accountTag.getTag().equals(tagEntity));
    }

    public void addAccountZoneEntity(AccountZoneEntity accountZoneEntity) {
        this.accountZoneSet.add(accountZoneEntity);
        accountZoneEntity.syncAccount(this);
    }

    public void removeAccountZoneEntity(ZoneEntity zoneEntity) {
        accountZoneSet.removeIf(accountZone -> accountZone.getZone().equals(zoneEntity));
    }

}
