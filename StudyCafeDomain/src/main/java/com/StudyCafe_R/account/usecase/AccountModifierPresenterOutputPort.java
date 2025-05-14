package com.StudyCafe_R.account.usecase;

import com.StudyCafe_R.account.domain.Account;

public interface AccountModifierPresenterOutputPort {

    /**
     * Retrieve the Base64-encoded profile image for an account.
     *
     * @param nicknameOrEmail unique identifier
     * @return Base64 JPEG/PNG string
     */
    void getProfileImage(String nicknameOrEmail);

    /**
     * List all tags on the given account, mapped into DTOs.
     *
     * @param accountId the PK of the account
     * @return list of TagDto
     */
    void listTags(long accountId);

    /**
     * List all zones on the given account, mapped into DTOs.
     *
     * @param accountId the PK of the account
     * @return list of ZoneDto
     */
    void listZones(long accountId);

    /**
     * Fetch the raw Account aggregate by nickname or email.
     *
     * @param nicknameOrEmail unique identifier
     * @return Account aggregate
     */
    void getAccount(String nicknameOrEmail);

    /**
     * Fetch a fully populated AccountDto (incl. tags, zones, image).
     *
     * @param nicknameOrEmail unique identifier
     * @return AccountDto
     */
    void getAccountDto(String nicknameOrEmail);
}
