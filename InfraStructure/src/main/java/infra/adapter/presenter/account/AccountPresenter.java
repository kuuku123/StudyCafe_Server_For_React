package infra.adapter.presenter.account;

import infra.adapter.presenter.CommonRestPresenter;
import infra.adapter.presenter.account.response.AccountDto;
import com.StudyCafe_R.usecase.account.AccountModifierPresenterOutputPort;
import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@RequiredArgsConstructor
@Component
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
