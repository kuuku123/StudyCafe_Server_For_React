package com.StudyCafe_R.account.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.StudyCafe_R.account.domain.Account;
import com.StudyCafe_R.account.port.db.AccountPersistenceOperationsOutputPort;

import java.io.IOException;
import java.io.InputStream;

import com.StudyCafe_R.account.usecase.command.CreateAccountCommand;
import com.StudyCafe_R.util.ClasspathAnonymousImageProvider;
import com.StudyCafe_R.util.ImageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountModifierUseCaseTest {

  @Mock
  private AccountModifierPresenterOutputPort presenter;

  @Mock
  private AccountPersistenceOperationsOutputPort persistenceOps;

  @Spy
  private ClasspathAnonymousImageProvider imageProvider
          = new ClasspathAnonymousImageProvider("static/images/anonymous.JPG");

  @InjectMocks
  private AccountModifierUseCase useCase;

  @Captor
  private ArgumentCaptor<Account> accountCaptor;

  private Account dummyAccount;

  @BeforeEach
  void setUp() {
    // Prepare a dummy domain account instance
    dummyAccount = Account.builder()
            .id(1L)
            .bio("default")
            .email("tony@example.com")
            .nickname("tony")
            .build();
  }

  @Test
  void whenRegisterAccount_thenAccountIsSavedWithAnonymousImage() throws Exception {
    // given
    byte[] anonymousBytes;
    CreateAccountCommand cmd = new CreateAccountCommand("tony", "tony@example.com");

    try {
      anonymousBytes = imageProvider.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // when
    useCase.registerAccount(cmd);

    // then
    // capture the Account passed to save(...)
    verify(persistenceOps,times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();

    // assert that the captured account’s profileImage is the anonymous JPG
    assertArrayEquals(
            anonymousBytes,
            savedAccount.getProfileImage(),
            "Profile image bytes should be set from anonymous.JPG"
    );
  }

  @Test
  void whenRegisterAccount_andAnonymousImageMissing_thenExceptionIsThrown() throws Exception {
    // simulate load failure stub
    when(imageProvider.load()).thenThrow(new IOException("not there"));

    CreateAccountCommand cmd = new CreateAccountCommand("tony", "tony@example.com");
    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> useCase.registerAccount(cmd),
            "Expected a RuntimeException when image loading fails"
    );

    assertTrue(
            ex.getMessage().contains("not there"),
            "Exception message should mention default image load failure"
    );

    // and nothing should have been saved
    verifyNoInteractions(persistenceOps);
  }
}