package infra.adapter.presenter.account;

import infra.adapter.presenter.account.request.SignUpRequest;
import com.StudyCafe_R.usecase.account.AccountModifierInputPort;
import com.StudyCafe_R.usecase.account.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountModifierInputPort accountModifierUseCase;

    @PostMapping("/sign-up")
    public void signUpSubmit(@RequestBody SignUpRequest signUpRequest) {
        accountModifierUseCase.registerAccount(new CreateAccountCommand(signUpRequest.getNickname(), signUpRequest.getEmail()));
    }
}
