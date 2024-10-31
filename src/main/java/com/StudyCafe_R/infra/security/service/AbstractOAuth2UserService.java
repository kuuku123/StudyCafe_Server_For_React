package com.StudyCafe_R.infra.security.service;

import com.StudyCafe_R.infra.security.ProviderUser;
import com.StudyCafe_R.infra.security.User;
import com.StudyCafe_R.infra.security.certification.SelfCertification;
import com.StudyCafe_R.infra.security.common.converters.ProviderUserConverter;
import com.StudyCafe_R.infra.security.common.converters.ProviderUserRequest;
import com.StudyCafe_R.infra.security.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@Getter
public abstract class AbstractOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SelfCertification certification;
    @Autowired
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public void selfCertificate(ProviderUser providerUser) {
        certification.checkCertification(providerUser);
    }

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {

        User user = userRepository.findByUsername(providerUser.getUsername());

        if (user == null) {
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            //TODO need to use Account
//            userService.register(clientRegistration.getRegistrationId(), providerUser);
        } else {
            System.out.println("userRequest = " + userRequest);
        }
    }

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.convert(providerUserRequest);
    }
}
