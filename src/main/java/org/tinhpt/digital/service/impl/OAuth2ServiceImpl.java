package org.tinhpt.digital.service.impl;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.service.OAuth2Service;
import org.tinhpt.digital.utils.GoogleUserInfo;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Override
    public GoogleUserInfo extractGoogleUserInfo(OAuth2User oAuth2User) {
        return GoogleUserInfo.builder()
                .email(oAuth2User.getAttribute("email"))
                .name(oAuth2User.getAttribute("name"))
                .pictureUrl(oAuth2User.getAttribute("picture"))
                .locale(oAuth2User.getAttribute("locale"))
                .familyName(oAuth2User.getAttribute("family_name"))
                .givenName(oAuth2User.getAttribute("given_name"))
                .build();
    }
}
