package com.StudyCafe_R.infra.security.common.converters;


import com.StudyCafe_R.infra.security.ProviderUser;
import com.StudyCafe_R.infra.security.common.enums.OAuth2Config;
import com.StudyCafe_R.infra.security.common.util.OAuth2Utils;
import com.StudyCafe_R.infra.security.social.GoogleUser;

public final class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.GOOGLE.getSocialName())) {
            return null;
        }

        return new GoogleUser(OAuth2Utils.getMainAttributes(
                providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
