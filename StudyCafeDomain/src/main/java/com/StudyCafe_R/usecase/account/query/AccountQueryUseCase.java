package com.StudyCafe_R.usecase.account.query;

import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.domain.Tag;
import com.StudyCafe_R.domain.Zone;
import com.StudyCafe_R.usecase.account.response.GetProfileResponse;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.db.TagPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.db.ZonePersistenceOperationsOutput;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.usecase.tag.response.TagResponse;
import com.StudyCafe_R.usecase.zone.response.ZoneResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountQueryUseCase implements AccountQueryInputPort {

    private final AccountQueryPresenterOutputPort presenter;
    private final AccountPersistenceOperationsOutputPort accountPersistenceOps;
    private final TagPersistenceOperationsOutputPort tagPersistenceOps;
    private final ZonePersistenceOperationsOutput zonePersistenceOps;
    private final TransactionOperationsOutputPort txOps;

    @Override
    public void getProfile(GetProfileByEmailQuery query) {
        try {
            txOps.doInTransaction(() -> {
                String email = query.email();
                Account account = accountPersistenceOps.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("No account found for email: " + email));

                Set<Tag> tags = tagPersistenceOps.findAllById(account.getTags())
                        .orElseThrow(() -> new RuntimeException("some tag doesn't exist"));
                Set<Zone> zones = zonePersistenceOps.findAllById(account.getZones())
                        .orElseThrow(() -> new RuntimeException("some zone doesn't exist"));

                Set<TagResponse> tagResponses = tags.stream()
                        .map(t -> new TagResponse(t.getId(), t.getTitle()))
                        .collect(Collectors.toSet());
                Set<ZoneResponse> zoneResponses = zones.stream()
                        .map(z -> new ZoneResponse(z.getId(), z.getCity(), z.getLocalNameOfCity(), z.getProvince()))
                        .collect(Collectors.toSet());

                presenter.presentMessageWhenSucceedGetProfile(new GetProfileResponse(account.getId(), account.getEmail(), account.getNickname(),
                        account.getBio(), account.getUrl(), account.getOccupation(), account.getLocation(), account.getProfileImageAsString(),tagResponses, zoneResponses));

            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }


    }
}
