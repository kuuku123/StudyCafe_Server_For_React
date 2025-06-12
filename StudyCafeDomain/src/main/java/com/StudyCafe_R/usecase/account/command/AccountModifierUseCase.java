package com.StudyCafe_R.usecase.account.command;

import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.account.AccountNotFoundException;
import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.util.ImageProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AccountModifierUseCase implements AccountModifierInputPort {

    private final AccountModifierPresenterOutputPort presenter;
    private final AccountPersistenceOperationsOutputPort persistenceOps;
    private final ImageProvider imageProvider;
    private final TransactionOperationsOutputPort txOps;

    @Override
    public void registerAccount(CreateAccountCommand command) {
        try {
            txOps.doInTransaction(() -> {
                Account account = Account.builder()
                        .nickname(command.nickname())
                        .email(command.email())
                        .build();

                try {
                    byte[] anonymousProfileJpg = imageProvider.load();
                    account.updateProfileImage(anonymousProfileJpg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                persistenceOps.save(account);

                // This now runs synchronously within the transaction
                presenter.presentMessageWhenSucceedRegisterAccount(new RegisterAccountResponse(account.getNickname(), account.getEmail(), account.getProfileImageAsString()));
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void updateProfile(UpdateProfileCommand command) {
        try {
            txOps.doInTransaction(() -> {
                Long accountId = command.accountId();
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "updateProfile"));

                String base64Image = command.profileImage();
                if (base64Image != null) {
                    if (base64Image.startsWith("data:image/jpeg;base64,")) {
                        base64Image = base64Image.substring("data:image/jpeg;base64,".length());
                    }
                    if (base64Image.startsWith("data:image/png;base64,")) {
                        base64Image = base64Image.substring("data:image/png;base64,".length());
                    }
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    account.updateProfileImage(imageBytes);
                }

                account.updateProfileDetails(
                        command.bio(),
                        command.url(),
                        command.occupation(),
                        command.location()
                );

                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedUpdateProfile();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void updateNotifications(UpdateNotificationCommand command) {
        try {
            txOps.doInTransaction(() -> {
                Long accountId = command.accountId();
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "updateNotifications"));

                account.updateNotificationDetails(
                        command.studyCreatedByEmail(),
                        command.studyCreatedByWeb(),
                        command.studyEnrollmentResultByEmail(),
                        command.studyEnrollmentResultByWeb(),
                        command.studyUpdatedByEmail(),
                        command.studyUpdatedByWeb()
                );

                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedUpdateNotification();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void addTag(long accountId, long tagId) {
        try {
            txOps.doInTransaction(() -> {
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "addTag"));

                account.addTag(tagId);
                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedAddTag();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void removeTag(long accountId, long tagId) {
        try {
            txOps.doInTransaction(() -> {
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "removeTag"));

                account.removeTag(tagId);
                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedRemoveTag();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void addZone(long accountId, long zoneId) {
        try {
            txOps.doInTransaction(() -> {
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "addZone"));

                account.addZone(zoneId);
                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedAddZone();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }

    @Override
    public void removeZone(long accountId, long zoneId) {
        try {
            txOps.doInTransaction(() -> {
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "removeZone"));

                account.removeZone(zoneId);
                persistenceOps.save(account);

                // CHANGED: Moved from doAfterCommit to run synchronously
                presenter.presentMessageWhenSucceedRemoveZone();
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }
}