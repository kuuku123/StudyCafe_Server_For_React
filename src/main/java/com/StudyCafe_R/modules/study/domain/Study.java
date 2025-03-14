package com.StudyCafe_R.modules.study.domain;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.modules.account.domain.AccountStudyMembers;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Study {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManager> managers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyMembers> members = new HashSet<>();

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
    private Set<StudyTag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyZone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    private int memberCount;

    public void addManager(AccountStudyManager accountStudyManager) {
        managers.add(accountStudyManager);
        accountStudyManager.setStudy(this);
    }
//TODO check if i need to set accountStudyManager study to null
    public void removeManager(Account account) {
        managers.stream()
                .filter(asm -> asm.getAccount().equals(account))
                .findAny().ifPresent(asm -> asm.setStudy(null));
        managers.removeIf(asm -> asm.getAccount().equals(account));
//        accountStudyManager.setStudy(null);
    }

    public void addMember(AccountStudyMembers accountStudyMembers) {
        members.add(accountStudyMembers);
        accountStudyMembers.setStudy(this);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        members.stream()
                        .filter(asm -> asm.getAccount().equals(account))
                                .findAny().ifPresent(asm -> asm.setStudy(null));
        members.removeIf(asm -> asm.getAccount().equals(account));
//        accountStudyMembers.setStudy(null);
        this.memberCount--;
    }

    public void addStudyTag(StudyTag studyTag) {
        this.tags.add(studyTag);
        studyTag.setStudy(this);
    }

    public void removeStudyTag(Tag tag) {
        tags.stream()
                        .filter(st -> st.getTag().equals(tag))
                                .findAny().ifPresent(st -> st.setStudy(null));
        tags.removeIf(st -> st.getTag().equals(tag));
    }

    public void addStudyZone(StudyZone studyZone) {
        this.zones.add(studyZone);
        studyZone.setStudy(this);
    }

    public void removeStudyZone(Zone zone) {
        zones.stream()
                        .filter(sz -> sz.getZone().equals(zone))
                                .findAny().ifPresent(sz -> sz.setStudy(null));
        zones.removeIf(sz -> sz.getZone().equals(zone));
    }


//    public boolean isJoinable(UserAccount userAccount) {
//        return this.isPublished() && this.isRecruiting() && !this.members.stream()
//                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
//    }
//
//    public boolean isMember(UserAccount userAccount) {
//        return this.members.stream()
//                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
//    }
//
//    public boolean isManager(UserAccount userAccount) {
//        return this.managers.stream()
//                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
//    }
    private boolean doesAccountExist(Account accountStudy, Account userAccount) {
        return accountStudy.getNickname().equals(userAccount.getNickname());
    }

    public void publish() {
        if (!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        }
        else {
            throw new RuntimeException("스터디를 공개할 수 없는 상태입니다. 스터디를 이미 공개했거나 종료했습니다.");
        }
    }

    public void close() {
        if (this.published && !this.closed) {
            this.closed = true;
            this.closedDateTime = LocalDateTime.now();
        }
        else {
            throw new RuntimeException("스터디를 종료할 수 없습니다. 스터디를 공개하지 않았거나 이미 종료한 스터디입니다.");
        }
    }

    public void startRecruit() {
        if (canUpdateRecruiting()) {
            this.recruiting = true;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        }
        else {
            throw new RuntimeException("인원 모집을 시작할 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 도전하세요.");
        }
    }

    public void stopRecruit() {
        if (canUpdateRecruiting()) {
            this.recruiting = false;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 멈출 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
    }
    public boolean canUpdateRecruiting() {
        return this.published && this.recruitingUpdatedDateTime == null || this.recruitingUpdatedDateTime.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isRemovable() {
        return !this.published; // TODO 모임을 했던 스터디는 삭제할 수 없다.
    }

    public String getEncodedPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public boolean isManagedby(Account account) {
        return this.getManagers().stream()
                .anyMatch(accountStudyManager -> accountStudyManager.getAccount().equals(account));
    }
}
