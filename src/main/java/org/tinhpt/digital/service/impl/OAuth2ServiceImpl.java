package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.OAuth2Service;
import org.tinhpt.digital.utils.GoogleUserInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private final UserRepository userRepository;

    @Override
    public GoogleUserInfo extractGoogleUserInfo(OAuth2User oAuth2User) {
        return GoogleUserInfo.builder()
                .email(oAuth2User.getAttribute("email"))
                .name(oAuth2User.getAttribute("name"))
                .pictureUrl(oAuth2User.getAttribute("picture"))
                .build();
    }

    @Override
    public OAuth2User processOAuthUser(OAuth2UserRequest request) {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);



        return oauth2User;
    }
}
