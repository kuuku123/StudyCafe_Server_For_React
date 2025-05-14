package com.StudyCafe_R.account.usecase;

import com.StudyCafe_R.account.domain.Account;
import com.StudyCafe_R.account.port.db.AccountPersistenceOperationsOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONUtil;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
public class AccountModifierUseCase implements AccountModifierInputPort{

    private final AccountModifierPresenterOutputPort presenter;
    private final AccountPersistenceOperationsOutputPort persistenceOps;
    private final ModelMapper modelMapper;


    @Override
    public void registerAccount(CreateAccountCommand command) {
        Account account = modelMapper.map(command, Account.class);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/images/anonymous.JPG")) {
            byte[] anonymousProfileJpg = inputStream.readAllBytes();
            account.updateProfileImage(anonymousProfileJpg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        persistenceOps.save(account);
    }

    @Override
    public void updateProfile(UpdateProfileCommand command) {

        Long accountId = command.getAccountId();
        Optional<Account> optionalAccount= persistenceOps.findById(accountId);
        Account account = optionalAccount.orElseThrow(() -> new AccountNotFoundException(accountId, "updateProfile"));

        String base64Image = command.getProfileImage();
        if(base64Image != null) {
            if (base64Image.startsWith("data:image/jpeg;base64,")) {
                base64Image = base64Image.substring("data:image/jpeg;base64,".length());
            }
            if (base64Image.startsWith("data:image/png;base64,")) {
                base64Image = base64Image.substring("data:image/png;base64,".length());
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            account.updateProfileImage(imageBytes);
        }
        account.updateProfileDetails(command.getBio(), command.getUrl(), command.getOccupation(), command.getLocation());
        persistenceOps.save(account); // merge detached entity
    }

    @Override
    public void updateNotifications(UpdateNotificationCommand command) {


    }

    @Override
    public void addTag(long accountId, long tagId) {

    }

    @Override
    public void removeTag(long accountId, long tagId) {

    }

    @Override
    public void addZone(long accountId, long zoneId) {

    }

    @Override
    public void removeZone(long accountId, long zoneId) {

    }
}
