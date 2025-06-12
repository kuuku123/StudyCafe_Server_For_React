package com.StudyCafe_R.usecase.account.query;

import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.account.response.GetProfileResponse;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.usecase.tag.response.TagResponse;
import com.StudyCafe_R.usecase.zone.response.ZoneResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountQueryUseCase implements AccountQueryInputPort {

    private final AccountQueryPresenterOutputPort presenter;
    private final AccountPersistenceOperationsOutputPort persistenceOps;
    private final TransactionOperationsOutputPort txOps;

    @Override
    public void getProfile(GetProfileByEmailQuery query) {
        String email = query.email();
        Account account = persistenceOps.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found for email: " + email));

        List<TagResponse> tagResponses;
        List<ZoneResponse> zoneResponses;

        presenter.presentMessageWhenSucceedGetProfile(new GetProfileResponse(account.getId(), account.getEmail(), account.getNickname(),
                account.getBio(), account.getUrl(), account.getOccupation(), account.getLocation(), account.getProfileImageAsString(),tagResponses, zoneResponses);

    }

}
