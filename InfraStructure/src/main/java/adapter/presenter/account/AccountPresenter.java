package adapter.presenter.account;

import adapter.presenter.CommonRestPresenter;
import adapter.presenter.account.response.AccountDto;
import com.StudyCafe_R.usecase.account.AccountModifierPresenterOutputPort;
import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountPresenter implements AccountModifierPresenterOutputPort {

    private final CommonRestPresenter commonRestPresenter;

    @Override
    public void presentMessageWhenSucceedRegisterAccount(RegisterAccountResponse registerAccountResponse) {
        commonRestPresenter.presentOk(AccountDto
                .builder()
                .nickname(registerAccountResponse.nickname())
                .email(registerAccountResponse.email())
                .profileImage(registerAccountResponse.profileImage())
                .build());
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
