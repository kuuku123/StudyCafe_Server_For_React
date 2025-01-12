package com.StudyCafe_R.infra.security.common.converters;

import com.StudyCafe_R.infra.security.ProviderUser;
import com.StudyCafe_R.infra.security.common.enums.OAuth2Config;
import com.StudyCafe_R.infra.security.common.util.OAuth2Utils;
import com.StudyCafe_R.infra.security.social.KakaoOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public final class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.KAKAO.getSocialName())) {
            return null;
        }

        if (!(providerUserRequest.oAuth2User() instanceof OidcUser)) {
            return null;
        }

        return new KakaoOidcUser(OAuth2Utils.getMainAttributes(
                providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
