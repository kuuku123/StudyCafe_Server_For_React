package infra.adapter.presenter.account;

import com.StudyCafe_R.usecase.account.query.AccountQueryPresenterOutputPort;
import com.StudyCafe_R.usecase.account.response.GetProfileResponse;
import infra.adapter.presenter.ApiResponse;
import infra.adapter.presenter.CommonRestPresenter;
import infra.adapter.presenter.account.response.AccountDto;
import infra.adapter.presenter.tag.response.TagDto;
import infra.adapter.presenter.zone.response.ZoneDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class AccountQueryPresenter implements AccountQueryPresenterOutputPort {

    private final CommonRestPresenter commonRestPresenter;

    @Override
    public void presentMessageWhenSucceedGetProfile(GetProfileResponse getProfileResponse) {
        AccountDto accountDto = AccountDto
                .builder()
                .email(getProfileResponse.email())
                .nickname(getProfileResponse.nickname())
                .bio(getProfileResponse.bio())
                .url(getProfileResponse.url())
                .occupation(getProfileResponse.occupation())
                .location(getProfileResponse.location())
                .profileImage(getProfileResponse.profileImage())
                .tags(getProfileResponse.tags().stream()
                        .map(tagResponse -> new TagDto(tagResponse.id(), tagResponse.title()))
                        .collect(Collectors.toSet()))
                .zones(getProfileResponse.zones().stream()
                        .map(zoneResponse -> new ZoneDto(zoneResponse.id(), zoneResponse.city(), zoneResponse.localNameOfCity(),
                                zoneResponse.province()))
                        .collect(Collectors.toSet()))
                .build();
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("profile", HttpStatus.OK, accountDto);
        commonRestPresenter.presentOk(apiResponse);
    }

    @Override
    public void presentError(Exception e) {
        commonRestPresenter.presentError(e);
    }
}
