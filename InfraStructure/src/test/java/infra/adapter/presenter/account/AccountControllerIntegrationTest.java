package infra.adapter.presenter.account;

import com.fasterxml.jackson.core.type.TypeReference;
import infra.adapter.database.account.AccountEntity;
import infra.adapter.database.account.AccountRepository;
import infra.adapter.presenter.ApiResponse;
import infra.adapter.presenter.MyConstants;
import infra.adapter.presenter.account.request.SignUpRequest;
import infra.adapter.presenter.account.response.AccountDto;
import infra.util.ClasspathAnonymousImageProvider;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MvcResult result = mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        // Get the raw JSON string from the response
        String responseBody = result.getResponse().getContentAsString();

        // Manually parse the JSON and assert on the object
        // Note: Assuming your ApiResponse class has getters or is a record
        ApiResponse<AccountDto> apiResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});

        assertThat(apiResponse.getMessage()).isEqualTo("sign up succeed");
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK);


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

    @Test
    @DisplayName("Get Profile: Should return account profile for existing user email")
    void getProfile_Success() throws Exception {
        // --- ARRANGE ---

        // load a real byte[] from test‐resources using your domain loader
        ClasspathAnonymousImageProvider imgProvider =
                new ClasspathAnonymousImageProvider("static/images/anonymous.JPG");
        byte[] imageBytes = imgProvider.load();  // throws if not found
        // create & persist a test account

        AccountEntity saved = accountRepository.save(
                AccountEntity.builder()
                        .nickname("test_nick")
                        .email("test@example.com")
                        .bio("A short bio")
                        .url("http://example.com")
                        .occupation("Tester")
                        .location("Seoul")
                        // this field holds your base64-encoded profile image string
                        .profileImage(imageBytes)
                        // default flags; adjust if your entity defaults differ
                        .studyCreatedByWeb(true)
                        .studyEnrollmentResultByWeb(true)
                        .build()
        );

        // --- ACT ---
        MvcResult result = mockMvc.perform(
                        get("/profile")
                                .header(MyConstants.HEADER_USER_EMAIL, saved.getEmail())
                )
                .andExpect(status().isOk())
                .andReturn();

        // --- ASSERT RESPONSE SHAPE & CONTENT ---
        String json = result.getResponse().getContentAsString();
        ApiResponse<AccountDto> apiResponse = objectMapper.readValue(
                json,
                new TypeReference<>() {
                }
        );

        assertThat(apiResponse.getMessage()).isEqualTo("profile");
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK);

        AccountDto dto = apiResponse.getData();

        String expectedBase64 = Base64.getEncoder().encodeToString(imageBytes);

        assertThat(dto.getEmail()).isEqualTo(saved.getEmail());
        assertThat(dto.getNickname()).isEqualTo(saved.getNickname());
        assertThat(dto.getBio()).isEqualTo(saved.getBio());
        assertThat(dto.getUrl()).isEqualTo(saved.getUrl());
        assertThat(dto.getOccupation()).isEqualTo(saved.getOccupation());
        assertThat(dto.getLocation()).isEqualTo(saved.getLocation());
        assertThat(dto.getProfileImage()).isEqualTo(expectedBase64);
        // since we didn't assign any tags or zones, they should be empty
        assertThat(dto.getTags()).isEmpty();
        assertThat(dto.getZones()).isEmpty();
    }

}