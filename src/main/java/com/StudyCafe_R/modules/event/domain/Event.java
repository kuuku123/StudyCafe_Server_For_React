package com.StudyCafe_R.modules.event.domain;

import com.StudyCafe_R.StudyCafe_R.modules.event.domain.EventType;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.study.domain.Study;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NamedEntityGraph(
        name = "Event.withEnrollments",
        attributeNodes = {
                @NamedAttributeNode("enrollments"),
        }
)
@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_event_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private Integer limitOfEnrollments;

    //TODO check if casacde if needed
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

//    public boolean isEnrollPossibleFor(UserAccount userAccount) {
//        return isNotClosed() && !isAttended(userAccount) &&
//                !isAlreadyEnrolled(userAccount);
//    }
//
//    public boolean isDisEnrollPossibleFor(UserAccount userAccount) {
//        return isNotClosed() && !isAttended(userAccount) &&
//                isAlreadyEnrolled(userAccount);
//    }

    public boolean isNotClosed() {
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

//    public boolean isAttended(UserAccount userAccount) {
//        Account account = userAccount.getAccount();
//        for (Enrollment enrollment : enrollments) {
//            if(enrollment.getAccount().equals(account) && enrollment.isAttended()) {
//                return true;
//            }
//        }
//        return false;
//    }

    public int numberOfRemainSpots() {
        return this.limitOfEnrollments - (int) this.enrollments.stream().filter(Enrollment::isAccepted).count();
    }

//    public boolean isAlreadyEnrolled(UserAccount userAccount) {
//        Account account = userAccount.getAccount();
//        for (Enrollment enrollment : enrollments) {
//            if(enrollment.getAccount().equals(account)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public long getNumberOfAcceptedEnrollments() {
        return this.enrollments.stream()
                .filter(Enrollment::isAccepted).count();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setEvent(null);
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        return this.eventType == EventType.FCFS && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments();
    }

    public boolean canAccept(Enrollment enrollment) {
        return this.eventType == EventType.CONFRIMATIVE
                && this.enrollments.contains(enrollment)
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()
                && !enrollment.isAttended()
                && !enrollment.isAccepted();
    }

    public boolean canReject(Enrollment enrollment) {
        return this.eventType == EventType.CONFRIMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && enrollment.isAccepted();
    }

    private List<Enrollment> getWaitingList() {
        return this.enrollments.stream()
                .filter(enrollment ->  !enrollment.isAccepted()).collect(Collectors.toList());
    }

    public void acceptWaitingList() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            List<Enrollment> waitingList = getWaitingList();
            int numberToAccept = (int) Math.min(this.limitOfEnrollments - this.getNumberOfAcceptedEnrollments(), waitingList.size());
            waitingList.subList(0,numberToAccept).forEach(e -> e.setAccepted(true));
        }
    }

    public void acceptNextWaitingEnrollment() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            Enrollment enrollmentToAccept = this.getTheFirstWaitingEnrollment();
            if(enrollmentToAccept != null) {
                enrollmentToAccept.setAccepted(true);
            }
        }
    }

    private Enrollment getTheFirstWaitingEnrollment() {
        for (Enrollment enrollment : enrollments) {
            if(!enrollment.isAccepted()) {
                return enrollment;
            }
        }
        return null;
    }

    public void accept(Enrollment enrollment) {
        if (this.eventType == EventType.CONFRIMATIVE && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()) {
            enrollment.setAccepted(true);
        }
    }

    public void reject(Enrollment enrollment) {
        if (this.eventType == EventType.CONFRIMATIVE) {
            enrollment.setAccepted(false);
        }
    }

}
