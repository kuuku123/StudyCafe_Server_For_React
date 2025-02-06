package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.AbstractContainerBaseTest;
import com.StudyCafe_R.infra.MockMvcTest;
import com.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest extends AbstractContainerBaseTest {

    @Autowired  MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @MockBean
    EmailService emailService;

    Cookie xsrfCookie;
    @BeforeEach
    void getXsrfToken() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/xsrf-token")).andReturn().getResponse();
        xsrfCookie = response.getCookie("XSRF-TOKEN");
    }

    @DisplayName("check verification email - invalid token case")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token","sldkjflsdlk")
                .param("email","email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("email/checked-email"))
                .andExpect(unauthenticated());
    }

    @DisplayName("check verification email - valid token case")
    @Test
    @Transactional
    void checkEmailToken_withTwoRequests() throws Exception {

        /*
         * Issue: `andExpect(authenticated())` in MockMvc does not interact with Redis for session management.
         * Solution:
         * - Manually retrieve the session cookie after login.
         * - Use the retrieved session cookie to call an API that requires authentication.
         */
        // 1) Create a test account with an email token
        Account account = Account.builder()
                .email("test@email.com")
                .password("12345678")
                .nickname("tony")
                .build();
        account.generateEmailCheckToken();
        accountRepository.save(account);

        // 2) Perform the first request (email verification)
        MvcResult result = mockMvc.perform(get("/check-email-token")
                        .param("token", account.getEmailCheckToken())
                        .param("email", account.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("email/checked-email"))
                // Don't check authenticated() here, it won't be recognized in the same request
                .andReturn();

        checkAuthentication(result);
    }

    private void checkAuthentication(MvcResult result) throws Exception {
        // 3) Extract the session cookie from the first response
        MockHttpServletResponse response = result.getResponse();
        Cookie sessionCookie = response.getCookie("SESSION");

        // 4) Perform a second request that requires authentication
        mockMvc.perform(get("/check-email").cookie(sessionCookie))
                .andExpect(status().isOk());
    }

//        RedisSessionRepository sessionRepo = (RedisSessionRepository) request.getAttribute("org.springframework.session.SessionRepository");
//org.springframework.session.web.http.CookieHttpSessionIdResolver.WRITTEN_SESSION_ID_ATTR



    @DisplayName("sign up - valid input")
    @Test
    void signUpSubmit_Success() throws Exception {
        SignUpRequest validSignUpForm = new SignUpRequest();
        validSignUpForm.setEmail("tony@gmail.com");
        validSignUpForm.setPassword("password123");
        validSignUpForm.setNickname("tony");

        mockMvc.perform(post("/sign-up").cookie(xsrfCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(validSignUpForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("sign up succeed"));

        Account account = accountRepository.findByEmail("tony@gmail.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(),"password123");
        assertNotNull(account.getEmailCheckToken());

        then(emailService).should().sendEmail(any(EmailMessage.class));
    }

    @DisplayName("sign up - invalid input")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        SignUpRequest validSignUpForm = new SignUpRequest();
        validSignUpForm.setEmail("invalid email");
        validSignUpForm.setPassword("password123");
        validSignUpForm.setNickname("tony");

        mockMvc.perform(post("/sign-up").cookie(xsrfCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(validSignUpForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("signup failed"));
    }
}