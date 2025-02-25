package org.tinhpt.digital.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.tinhpt.digital.utils.GoogleUserInfo;

public interface OAuth2Service {
    GoogleUserInfo extractGoogleUserInfo(OAuth2User oAuth2User);

    OAuth2User processOAuthUser(OAuth2UserRequest request);

}
