package com.StudyCafe_R.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import com.StudyCafe_R.account.domain.Account;
import com.StudyCafe_R.account.port.db.AccountPersistenceOperationsOutputPort;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountModifierUseCaseTest {

  @Mock
  private AccountModifierPresenterOutputPort presenter;

  @Mock
  private AccountPersistenceOperationsOutputPort persistenceOps;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private AccountModifierUseCase useCase;

  private Account dummyAccount;
  private byte[] anonymousBytes;

  @BeforeEach
  void setUp() throws Exception {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Prepare a dummy domain account instance
    dummyAccount = new Account(/* supply required ctor args if any */);
    // lenient stub so that unused stubbings don't fail
    lenient().when(modelMapper.map(any(CreateAccountCommand.class), eq(Account.class)))
      .thenReturn(dummyAccount);

    // Pre-load the same anonymous image bytes that the use case will load
    try (InputStream is =
      getClass().getClassLoader().getResourceAsStream("static/images/anonymous.JPG")) {
      assertNotNull(is, "Test resource not found: static/images/anonymous.JPG");
      anonymousBytes = is.readAllBytes();
    }
  }

  @Test
  void whenRegisterAccount_thenAccountIsMappedAndSavedWithAnonymousImage() {
    // given
    CreateAccountCommand cmd = new CreateAccountCommand(/* populate fields */);

    // when
    useCase.registerAccount(cmd);

    // then
    // 1. modelMapper should have been called to map the command
    verify(modelMapper).map(cmd, Account.class);

    // 2. the account’s profile image bytes should match the anonymous JPG
    assertArrayEquals(anonymousBytes, dummyAccount.getProfileImage(),
      "Profile image bytes should be set from anonymous.JPG");

    // 3. persistenceOps.save must be invoked with the same domain object
    verify(persistenceOps).save(dummyAccount);
  }

  @Test
  void whenResourceLoadFails_thenRuntimeExceptionIsThrown() throws Exception {
    // given: spy on the use case class to throw IOException
    AccountModifierUseCase spyUseCase = spy(useCase);
    doThrow(new IOException("oops"))
      .when(spyUseCase)
      .getClassLoaderStream(eq("static/images/anonymous.JPG"));

    CreateAccountCommand cmd = new CreateAccountCommand(/* fields */);

    // when / then
    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> spyUseCase.registerAccount(cmd));

    assertTrue(ex.getCause() instanceof IOException);
    assertEquals("oops", ex.getCause().getMessage());
  }

// Helper in the UseCase to ease stubbing classloader in tests
// You’ll need to add this to your AccountModifierUseCase for the second test:
//
// protected InputStream getClassLoaderStream(String path) throws IOException {
//     InputStream is = getClass().getClassLoader().getResourceAsStream(path);
//     if (is == null) {
//         throw new IOException("Resource not found: " + path);
//     }
//     return is;
// }
}