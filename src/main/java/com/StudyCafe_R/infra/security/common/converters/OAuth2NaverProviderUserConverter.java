package com.StudyCafe_R.infra.security.common.converters;


import com.StudyCafe_R.infra.security.ProviderUser;
import com.StudyCafe_R.infra.security.common.enums.OAuth2Config;
import com.StudyCafe_R.infra.security.common.util.OAuth2Utils;
import com.StudyCafe_R.infra.security.social.NaverUser;

public final class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.NAVER.getSocialName())) {
            return null;
        }

        return new NaverUser(OAuth2Utils.getSubAttributes(
                providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
