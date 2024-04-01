package com.StudyCafe_R.modules.account.domain;

import com.StudyCafe_R.modules.event.domain.Enrollment;
import com.StudyCafe_R.modules.event.domain.Event;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name= "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified; // check if current account is verified with email account

    private String emailCheckToken; // token used for email verification

    private LocalDateTime joinedAt; // specific time when email verification succeed and consider this user is signed in

    //client's extra info

    private String bio;

    private String url;

    private String occupation;

    private String location; // varchar(255) above all info

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    @Builder.Default
    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    @Builder.Default
    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    @Builder.Default
    private boolean studyUpdatedByWeb = true;
    private LocalDateTime emailCheckTokenGeneratedAt;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountTag> accountTagSet = new HashSet<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountZone> accountZoneSet = new HashSet<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManager> managers = new HashSet<>();


    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManager> members = new HashSet<>();

    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmationEmail() {
//        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
        return true;
    }

    public void addAccountTag(AccountTag accountTag) {
        this.accountTagSet.add(accountTag);
        accountTag.setAccount(this);
    }
//TODO check if i need to set accountTag account to null
    public void removeAccountTag(Tag tag) {
        accountTagSet.stream()
                        .filter(at -> at.getTag().equals(tag))
                                .findAny().ifPresent(at -> at.setAccount(null));
        accountTagSet.removeIf(accountTag -> accountTag.getTag().equals(tag));
    }

    public void addAccountZone(AccountZone accountZone) {
        this.accountZoneSet.add(accountZone);
        accountZone.setAccount(this);
    }

    public void removeAccountZone(Zone zone) {
        accountZoneSet.stream()
                        .filter(az -> az.getZone().equals(zone))
                                .findAny().ifPresent(az -> az.setAccount(null));
        accountZoneSet.removeIf(accountZone -> accountZone.getZone().equals(zone));
    }

}
