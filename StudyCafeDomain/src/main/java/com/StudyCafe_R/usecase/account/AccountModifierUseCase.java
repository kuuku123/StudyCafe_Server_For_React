package com.StudyCafe_R.usecase.account;

import com.StudyCafe_R.domain.Account;
import com.StudyCafe_R.usecase.account.response.RegisterAccountResponse;
import com.StudyCafe_R.usecase.port.db.AccountPersistenceOperationsOutputPort;
import com.StudyCafe_R.usecase.port.transaction.TransactionOperationsOutputPort;
import com.StudyCafe_R.usecase.account.command.CreateAccountCommand;
import com.StudyCafe_R.usecase.account.command.UpdateNotificationCommand;
import com.StudyCafe_R.usecase.account.command.UpdateProfileCommand;
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
                        .nickname(command.getNickname())
                        .email(command.getEmail())
                        .build();

                try {
                    byte[] anonymousProfileJpg = imageProvider.load();
                    account.updateProfileImage(anonymousProfileJpg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                persistenceOps.save(account);
                txOps.doAfterCommit(() -> presenter.presentMessageWhenSucceedRegisterAccount(new RegisterAccountResponse(account.getNickname(), account.getEmail(), account.getProfileImageAsString())));
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
                Long accountId = command.getAccountId();
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "updateProfile"));

                String base64Image = command.getProfileImage();
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
                        command.getBio(),
                        command.getUrl(),
                        command.getOccupation(),
                        command.getLocation()
                );

                persistenceOps.save(account); // merge detached entity
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedUpdateProfile);
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
                Long accountId = command.getAccountId();
                Account account = persistenceOps.findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId, "updateNotifications"));

                account.updateNotificationDetails(
                        command.isStudyCreatedByEmail(),
                        command.isStudyUpdatedByEmail(),
                        command.isStudyEnrollmentResultByEmail(),
                        command.isStudyEnrollmentResultByWeb(),
                        command.isStudyUpdatedByEmail(),
                        command.isStudyUpdatedByWeb()
                );

                persistenceOps.save(account);
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedUpdateNotification);
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
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedAddTag);
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
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedRemoveTag);
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
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedAddZone);
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
                txOps.doAfterCommit(presenter::presentMessageWhenSucceedRemoveZone);
            });
        } catch (Exception e) {
            e.printStackTrace();
            presenter.presentError(e);
        }
    }
}
