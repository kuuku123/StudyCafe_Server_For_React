package infra.adapter.presenter.account;

import com.StudyCafe_R.usecase.account.query.AccountQueryInputPort;
import com.StudyCafe_R.usecase.account.query.GetProfileByEmailQuery;
import infra.adapter.presenter.MyConstants;
import infra.adapter.presenter.account.request.SignUpRequest;
import com.StudyCafe_R.usecase.account.command.AccountModifierInputPort;
import com.StudyCafe_R.usecase.account.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountModifierInputPort accountModifierInputPort;
    private final AccountQueryInputPort accountQueryInputPort;

    @GetMapping("/profile")
    public void profile(@RequestHeader(MyConstants.HEADER_USER_EMAIL) String email) {
        accountQueryInputPort.getProfile(new GetProfileByEmailQuery(email));
    }


    @PostMapping("/sign-up")
    public void signUpSubmit(@RequestBody SignUpRequest signUpRequest) {
        accountModifierInputPort.registerAccount(new CreateAccountCommand(signUpRequest.getNickname(), signUpRequest.getEmail()));
    }

}
