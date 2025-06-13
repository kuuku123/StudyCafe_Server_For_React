package com.StudyCafe_R.usecase.account.query;

import com.StudyCafe_R.usecase.account.response.GetProfileResponse;
import com.StudyCafe_R.usecase.port.presenter.ErrorHandlingPresenterOutputPort;

public interface AccountQueryPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentMessageWhenSucceedGetProfile(GetProfileResponse getProfileResponse);
}
