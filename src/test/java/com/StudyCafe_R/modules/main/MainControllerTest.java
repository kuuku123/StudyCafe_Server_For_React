package com.StudyCafe_R.modules.main;

import com.StudyCafe_R.infra.AbstractContainerBaseTest;
import com.StudyCafe_R.infra.MockMvcTest;
import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.infra.microservice.dto.SignUpRequest;
import com.StudyCafe_R.modules.account.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class MainControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setNickname("tony");
        signUpRequest.setEmail("tony@email.com");
        accountService.processNewAccount(signUpRequest);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 성공")
    @ParameterizedTest
    @ValueSource(strings = {"tony","tony@email.com"})
    void login_with_email(String input) throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", input)
                        .param("password","12345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username","11111111")
                .param("password","0000000000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}