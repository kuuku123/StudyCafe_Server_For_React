package infra.config;

import com.StudyCafe_R.usecase.account.command.AccountModifierPresenterOutputPort;
import com.StudyCafe_R.usecase.account.command.AccountModifierInputPort;
import com.StudyCafe_R.usecase.account.command.AccountModifierUseCase;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.util.ClasspathAnonymousImageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AccountModifierInputPort accountModifierUseCase( AccountModifierPresenterOutputPort accountModifierPresenterOutputPort,
                                                            AccountPersistenceOperationsOutputPort persistenceOps,
                                                           TransactionOperationsOutputPort txOps) {
        return new AccountModifierUseCase(accountModifierPresenterOutputPort, persistenceOps, new ClasspathAnonymousImageProvider("static/images/anonymous.JPG"), txOps);
    }

}
