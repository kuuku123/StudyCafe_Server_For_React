package infra.adapter.presenter.account;

import com.StudyCafe_R.util.ImageProvider;
import infra.adapter.database.account.AccountEntity;
import infra.adapter.database.account.AccountRepository;
import infra.adapter.presenter.account.request.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers // Enables Testcontainers support for JUnit 5
@Transactional // Rolls back the transaction after each test to ensure isolation
public class AccountControllerIntegrationTest {

    // This will start a MySQL 8.0 Docker container for this test class
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    // This method dynamically sets the datasource properties for Spring before the app starts
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        // Important: Use a schema validation/update strategy suitable for tests
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // We REMOVED the @TestBean for AccountPersistenceOperationsOutputPort
    // Spring will now create the REAL JPA implementation.

    // We still might want to mock external services that are not the DB
    @MockitoBean
    private ImageProvider imageProvider = Mockito.mock(ImageProvider.class);

    // Although @Transactional handles rollback, explicitly cleaning the repository
    // can prevent side effects if you have tests that are not transactional.
    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Sign Up: Should successfully register a new account in the database")
    void signUpSubmit_Success() throws Exception {
        // --- ARRANGE ---
        SignUpRequest signUpRequest = new SignUpRequest("real_user", "real@example.com");
        String requestBody = objectMapper.writeValueAsString(signUpRequest);
//
        // --- ACT & ASSERT ---
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(signUpRequest.getNickname()))
                .andExpect(jsonPath("$.email").value(signUpRequest.getEmail()));

        // --- VERIFY DATABASE STATE ---
        // Use the repository to find the user that should have been created.
        Optional<AccountEntity> savedAccountOptional = accountRepository.findByEmail(signUpRequest.getEmail());

        // Assert that the user was actually found in the database.
        assertThat(savedAccountOptional).isPresent();

        // Unwrap the optional and perform detailed assertions on the entity's state.
        AccountEntity savedAccount = savedAccountOptional.get();
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getNickname()).isEqualTo(signUpRequest.getNickname());
        assertThat(savedAccount.getEmail()).isEqualTo(signUpRequest.getEmail());
        assertThat(savedAccount.getProfileImage())
                .isNotNull()
                .isNotEmpty();

        // You can also verify default values set by your entity or domain model
        assertThat(savedAccount.isStudyCreatedByWeb()).isTrue();
        assertThat(savedAccount.isStudyEnrollmentResultByWeb()).isTrue();
    }
}