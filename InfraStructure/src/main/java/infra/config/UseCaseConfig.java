package infra.config;

import com.StudyCafe_R.usecase.account.command.AccountModifierPresenterOutputPort;
import com.StudyCafe_R.usecase.account.command.AccountModifierInputPort;
import com.StudyCafe_R.usecase.account.command.AccountModifierUseCase;
import com.StudyCafe_R.usecase.account.query.AccountQueryInputPort;
import com.StudyCafe_R.usecase.account.query.AccountQueryUseCase;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.db.TagPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.db.ZonePersistenceOperationsOutput;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import infra.util.ClasspathAnonymousImageProvider;
import infra.adapter.presenter.account.AccountQueryPresenter;
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

    @Bean
    public AccountQueryInputPort accountQueryUseCase(AccountQueryPresenter accountQueryPresenter,
                                                     AccountPersistenceOperationsOutputPort accountPersistenceOps,
                                                     TagPersistenceOperationsOutputPort tagPersistenceOps,
                                                     ZonePersistenceOperationsOutput zonePersistenceOps,
                                                     TransactionOperationsOutputPort txOps) {
        return new AccountQueryUseCase(accountQueryPresenter,accountPersistenceOps,tagPersistenceOps, zonePersistenceOps,txOps);
    }

}
