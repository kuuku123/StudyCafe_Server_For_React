package com.StudyCafe_R.modules.account;

import com.StudyCafe_R.modules.account.domain.QAccount;
import com.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.modules.zone.Zone;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        return account.accountZoneSet.any().zone.in(zones).or(account.accountTagSet.any().tag.in(tags));
    }
}
