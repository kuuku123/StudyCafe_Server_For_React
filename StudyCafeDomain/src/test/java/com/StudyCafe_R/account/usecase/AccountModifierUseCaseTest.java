package com.StudyCafe_R.account.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.StudyCafe_R.account.domain.Account;
import com.StudyCafe_R.account.port.db.AccountPersistenceOperationsOutputPort;

import com.StudyCafe_R.account.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.account.port.transaction.TransactionRunnableWithoutResult;
import com.StudyCafe_R.account.usecase.command.UpdateNotificationCommand;
import java.io.IOException;

import com.StudyCafe_R.account.usecase.command.CreateAccountCommand;
import com.StudyCafe_R.util.ClasspathAnonymousImageProvider;
import java.util.Optional;
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

  @Mock
  private TransactionOperationsOutputPort txOps;


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

    doAnswer(invocation -> {
      TransactionRunnableWithoutResult unitOfWork = invocation.getArgument(0, TransactionRunnableWithoutResult.class);
      unitOfWork.run();
      return null;
    }).when(txOps).doInTransaction(any(TransactionRunnableWithoutResult.class));
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

  @Test
  void whenUpdateNotification_thenNotificationStatusIsChanged() throws Exception {

    // given
    UpdateNotificationCommand command = new UpdateNotificationCommand(
      1L,
      true,   // studyCreatedByEmail
      false,  // studyUpdatedByEmail
      true,   // studyEnrollmentResultByEmail
      false,  // studyEnrollmentResultByWeb
      false,  // (duplicate) studyUpdatedByEmail
      true    // studyUpdatedByWeb
    );
    when(persistenceOps.findById(1L)).thenReturn(Optional.of(dummyAccount));

    // when
    useCase.updateNotifications(command);

    // then
    verify(persistenceOps).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();

    assertTrue(savedAccount.isStudyCreatedByEmail(),       "Created‐by‐email flag should be true");
    assertFalse(savedAccount.isStudyUpdatedByEmail(),      "Updated‐by‐email flag should be false");
    assertTrue(savedAccount.isStudyEnrollmentResultByEmail(),  "Enrollment‐result‐by‐email flag should be true");
    assertFalse(savedAccount.isStudyEnrollmentResultByWeb(),   "Enrollment‐result‐by‐web flag should be false");
    assertFalse(savedAccount.isStudyUpdatedByEmail(),      "Duplicate updated‐by‐email flag should remain false");
    assertTrue(savedAccount.isStudyUpdatedByWeb(),         "Updated‐by‐web flag should be true");

  }

  @Test
  void whenAddTag_andAccountExists_thenTagIsAddedAndSaved() {
    long accountId = dummyAccount.getId();
    long newTagId = 42L;

    when(persistenceOps.findById(accountId))
      .thenReturn(Optional.of(dummyAccount));

    useCase.addTag(accountId, newTagId);

    verify(persistenceOps, times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();
    assertTrue(savedAccount.getTags().contains(newTagId));
  }

  @Test
  void whenAddTag_andAccountNotFound_thenThrowExceptionAndNoSave() {
    long missingAccountId = 999L;
    long someTagId = 5L;

    when(persistenceOps.findById(missingAccountId))
      .thenReturn(Optional.empty());

    AccountNotFoundException ex = assertThrows(
      AccountNotFoundException.class,
      () -> useCase.addTag(missingAccountId, someTagId)
    );
    assertTrue(ex.getMessage().contains("addTag"));
    verify(persistenceOps, never()).save(any(Account.class));
  }

  @Test
  void whenRemoveTag_andAccountExists_thenTagIsRemovedAndSaved() {
    long accountId = dummyAccount.getId();
    long existingTagId = 88L;
    dummyAccount.addTag(existingTagId);

    when(persistenceOps.findById(accountId))
      .thenReturn(Optional.of(dummyAccount));

    // Precondition: account has that tag
    assertTrue(dummyAccount.getTags().contains(existingTagId));

    useCase.removeTag(accountId, existingTagId);

    verify(persistenceOps, times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();
    assertFalse(savedAccount.getTags().contains(existingTagId));
  }

  @Test
  void whenRemoveTag_andAccountNotFound_thenThrowExceptionAndNoSave() {
    long missingAccountId = 999L;
    long someTagId = 5L;

    when(persistenceOps.findById(missingAccountId))
      .thenReturn(Optional.empty());

    AccountNotFoundException ex = assertThrows(
      AccountNotFoundException.class,
      () -> useCase.removeTag(missingAccountId, someTagId)
    );
    assertTrue(ex.getMessage().contains("removeTag"));
    verify(persistenceOps, never()).save(any(Account.class));
  }

  @Test
  void whenAddZone_andAccountExists_thenZoneIsAddedAndSaved() {
    long accountId = dummyAccount.getId();
    long newZoneId = 100L;

    when(persistenceOps.findById(accountId))
      .thenReturn(Optional.of(dummyAccount));

    useCase.addZone(accountId, newZoneId);

    verify(persistenceOps, times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();
    assertTrue(savedAccount.getZones().contains(newZoneId));
  }

  @Test
  void whenAddZone_andAccountNotFound_thenThrowExceptionAndNoSave() {
    long missingAccountId = 999L;
    long someZoneId = 7L;

    when(persistenceOps.findById(missingAccountId))
      .thenReturn(Optional.empty());

    AccountNotFoundException ex = assertThrows(
      AccountNotFoundException.class,
      () -> useCase.addZone(missingAccountId, someZoneId)
    );
    assertTrue(ex.getMessage().contains("addZone"));
    verify(persistenceOps, never()).save(any(Account.class));
  }

  @Test
  void whenRemoveZone_andAccountExists_thenZoneIsRemovedAndSaved() {
    long accountId = dummyAccount.getId();
    long existingZoneId = 50L;
    dummyAccount.addZone(existingZoneId);

    when(persistenceOps.findById(accountId))
      .thenReturn(Optional.of(dummyAccount));

    assertTrue(dummyAccount.getZones().contains(existingZoneId));

    useCase.removeZone(accountId, existingZoneId);

    verify(persistenceOps, times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();
    assertFalse(savedAccount.getZones().contains(existingZoneId));
  }

  @Test
  void whenRemoveZone_andAccountNotFound_thenThrowExceptionAndNoSave() {
    long missingAccountId = 999L;
    long someZoneId = 7L;

    when(persistenceOps.findById(missingAccountId))
      .thenReturn(Optional.empty());

    AccountNotFoundException ex = assertThrows(
      AccountNotFoundException.class,
      () -> useCase.removeZone(missingAccountId, someZoneId)
    );
    assertTrue(ex.getMessage().contains("removeZone"));
    verify(persistenceOps, never()).save(any(Account.class));
  }


}