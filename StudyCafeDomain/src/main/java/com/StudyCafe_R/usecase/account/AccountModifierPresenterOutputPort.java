package com.StudyCafe_R.usecase.account;

import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import com.StudyCafe_R.usecase.port.presenter.ErrorHandlingPresenterOutputPort;

public interface AccountModifierPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentMessageWhenSucceedRegisterAccount(RegisterAccountResponse registerAccountResponse);

    void presentMessageWhenSucceedUpdateProfile();

    void presentMessageWhenSucceedUpdateNotification();

    void presentMessageWhenSucceedAddTag();

    void presentMessageWhenSucceedRemoveTag();

    void presentMessageWhenSucceedAddZone();

    void presentMessageWhenSucceedRemoveZone();

}
