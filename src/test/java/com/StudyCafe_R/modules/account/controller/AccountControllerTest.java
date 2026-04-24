package com.StudyCafe_R.modules.account.controller;

import com.StudyCafe_R.infra.AbstractContainerBaseTest;
import com.StudyCafe_R.infra.MockMvcTest;
import com.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.infra.util.MyConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest extends AbstractContainerBaseTest {

    @Autowired  MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @MockBean
    EmailService emailService;
    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("sign up - valid input")
    @Test
    void signUpSubmit_Success() throws Exception {
        SignUpRequest validSignUpForm = new SignUpRequest();
        validSignUpForm.setEmail("tony@gmail.com");
        validSignUpForm.setNickname("tony");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignUpForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("sign up succeed"));

        Account account = accountRepository.findByEmail("tony@gmail.com");
        assertNotNull(account);
    }

    @DisplayName("sign up - invalid input")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        SignUpRequest validSignUpForm = new SignUpRequest();
        validSignUpForm.setEmail("invalid email");
        validSignUpForm.setNickname("tony");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSignUpForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("signup failed"));
    }

    @DisplayName("sign up - duplicated email should be blocked by validator")
    @Test
    void signUpSubmit_with_duplicated_email() throws Exception {
        // 1. Create an existing account
        Account account = Account.builder()
                .email("tony@gmail.com")
                .nickname("tony_first")
                .build();
        accountRepository.save(account);

        // 2. Try to sign up with the exact same email
        SignUpRequest duplicateEmailForm = new SignUpRequest();
        duplicateEmailForm.setEmail("tony@gmail.com");
        duplicateEmailForm.setNickname("tony_second");

        // 3. This should return 400 Bad Request because of SignUpFormValidator
        // BUT it currently will NOT, which proves the validator is disconnected!
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmailForm)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("get profile - success")
    @Test
    void profile_Success() throws Exception {
        // 1. Create a test account
        Account account = Account.builder()
                .email("tony@gmail.com")
                .nickname("tony")
                .build();
        accountRepository.save(account);

        // 2. Request profile with X-User-Email header
        mockMvc.perform(get("/profile")
                        .header(MyConstants.HEADER_USER_EMAIL, "tony@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("profile"))
                .andExpect(jsonPath("$.data.email").value("tony@gmail.com"))
                .andExpect(jsonPath("$.data.nickname").value("tony"));
    }

    @DisplayName("get other profile - success")
    @Test
    void otherProfile_Success() throws Exception {
        // 1. Create a test account
        Account account = Account.builder()
                .email("other@gmail.com")
                .nickname("other")
                .build();
        accountRepository.save(account);

        // 2. Request profile by path variable
        mockMvc.perform(get("/profile/other@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("profile"))
                .andExpect(jsonPath("$.data.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.data.nickname").value("other"));
    }
}