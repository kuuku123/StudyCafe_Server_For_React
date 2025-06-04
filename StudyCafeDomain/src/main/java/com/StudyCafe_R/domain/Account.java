package com.StudyCafe_R.domain;

import lombok.*;

import java.io.Serializable;
import java.util.*;

//AggregateRoot
@Getter
public class Account {

    private Long id;

    private String email;
    private String nickname;

    //client's extra info

    private String bio;
    private String url;
    private String occupation;
    private String location; // varchar(255) above all info

    private byte[] profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    private Set<Long> events = new HashSet<>();

    private Set<Long> enrollments = new HashSet<>();

    private Set<Long> tags = new HashSet<>();

    private Set<Long> zones = new HashSet<>();


    @Builder
    public Account(Long id, String email, String nickname, String bio, String url, String occupation, String location) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.url = url;
        this.occupation = occupation;
        this.location = location;
    }

    public void updateProfileImage(byte[] img) {
        this.profileImage = img;
    }

    public void updateProfileDetails(
            String bio,
            String url,
            String occupation,
            String location
    ) {
        this.bio = bio;
        this.url = url;
        this.occupation = occupation;
        this.location = location;
    }

    public void updateNotificationDetails(
            boolean studyCreatedByEmail,
            boolean studyCreatedByWeb,
            boolean studyEnrollmentResultByEmail,
            boolean studyEnrollmentResultByWeb,
            boolean studyUpdatedByEmail,
            boolean studyUpdatedByWeb
    ) {
        this.studyCreatedByEmail = studyCreatedByEmail;
        this.studyCreatedByWeb   = studyCreatedByWeb;
        this.studyEnrollmentResultByEmail = studyEnrollmentResultByEmail;
        this.studyEnrollmentResultByWeb   = studyEnrollmentResultByWeb;
        this.studyUpdatedByEmail  = studyUpdatedByEmail;
        this.studyUpdatedByWeb    = studyUpdatedByWeb;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public String getProfileImageAsString() {
        return Base64.getEncoder().encodeToString(profileImage);
    }

    // Called by the use‐case to attach a tag
    public void addTag(Long tagId) {
        if (tagId == null) {
            throw new IllegalArgumentException("tagId must not be null");
        }
        tags.add(tagId);
    }

    public void removeTag(Long tagId) {
        tags.remove(tagId);
    }

    // Similarly for zones
    public void addZone(Long zoneId) {
        if (zoneId == null) {
            throw new IllegalArgumentException("zoneId must not be null");
        }
        zones.add(zoneId);
    }

    public void removeZone(Long zoneId) {
        zones.remove(zoneId);
    }

}
