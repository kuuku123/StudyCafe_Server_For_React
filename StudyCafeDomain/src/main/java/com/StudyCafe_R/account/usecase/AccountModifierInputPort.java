package com.StudyCafe_R.account.usecase;

import com.StudyCafe_R.account.domain.Account;

public interface AccountModifierInputPort {

    /**
     * Register a new account using the provided data.
     *
     * @param command contains nickname, email, etc.
     * @return the newly created Account aggregate
     */
    void registerAccount(CreateAccountCommand command);


    /**
     * Update profile details, including setting/replacing the image.
     *
     * @param command contains accountId and new profile data
     */
    void updateProfile(UpdateProfileCommand command);

    /**
     * Update notification settings for an account.
     *
     * @param command contains accountId and notification preferences
     */
    void updateNotifications(UpdateNotificationCommand command);

    /**
     * Attach the given tag to the account (no-op if already attached).
     *
     * @param accountId the PK of the account
     * @param tagId     the PK of the tag
     */
    void addTag(long accountId, long tagId);

    /**
     * Remove the given tag from the account.
     *
     * @param accountId the PK of the account
     * @param tagId     the PK of the tag
     */
    void removeTag(long accountId, long tagId);


    /**
     * Attach the given zone to the account (no-op if already attached).
     *
     * @param accountId the PK of the account
     * @param zoneId    the PK of the zone
     */
    void addZone(long accountId, long zoneId);

    /**
     * Remove the given zone from the account.
     *
     * @param accountId the PK of the account
     * @param zoneId    the PK of the zone
     */
    void removeZone(long accountId, long zoneId);



}
