package infra.config;

import infra.adapter.presenter.CommonRestPresenter;
import infra.adapter.presenter.account.AccountPresenter;
import com.StudyCafe_R.usecase.account.AccountModifierInputPort;
import com.StudyCafe_R.usecase.account.AccountModifierUseCase;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.util.ClasspathAnonymousImageProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class UseCaseConfig {

    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public AccountModifierInputPort accountModifierUseCase(AccountPersistenceOperationsOutputPort persistenceOps,
                                                           HttpServletResponse httpServletResponse,
                                                           MappingJackson2HttpMessageConverter jackson2HttpMessageConverter,
                                                           TransactionOperationsOutputPort txOps) {
        return new AccountModifierUseCase(new AccountPresenter(new CommonRestPresenter(httpServletResponse, jackson2HttpMessageConverter)), persistenceOps, new ClasspathAnonymousImageProvider("static/images/anonymous.JPG"), txOps);
    }


}
