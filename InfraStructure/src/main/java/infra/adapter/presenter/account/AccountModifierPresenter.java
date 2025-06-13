package infra.adapter.presenter.account;

import infra.adapter.presenter.ApiResponse;
import infra.adapter.presenter.CommonRestPresenter;
import infra.adapter.presenter.account.response.AccountDto;
import com.StudyCafe_R.usecase.account.command.AccountModifierPresenterOutputPort;
import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountModifierPresenter implements AccountModifierPresenterOutputPort {

    private final CommonRestPresenter commonRestPresenter;

    @Override
    public void presentMessageWhenSucceedRegisterAccount(RegisterAccountResponse registerAccountResponse) {
        AccountDto accountDto = AccountDto
                .builder()
                .nickname(registerAccountResponse.nickname())
                .email(registerAccountResponse.email())
                .profileImage(registerAccountResponse.profileImage())
                .build();
        ApiResponse<AccountDto> apiResponse = new ApiResponse<>("sign up succeed", HttpStatus.OK, accountDto);
        commonRestPresenter.presentOk(apiResponse);
    }

    @Override
    public void presentMessageWhenSucceedUpdateProfile() {

    }

    @Override
    public void presentMessageWhenSucceedUpdateNotification() {

    }

    @Override
    public void presentMessageWhenSucceedAddTag() {

    }

    @Override
    public void presentMessageWhenSucceedRemoveTag() {

    }

    @Override
    public void presentMessageWhenSucceedAddZone() {

    }

    @Override
    public void presentMessageWhenSucceedRemoveZone() {

    }

    @Override
    public void presentError(Exception e) {
        commonRestPresenter.presentError(e);
    }
}
