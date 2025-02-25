package org.tinhpt.digital.helper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.tinhpt.digital.config.auth.JwtTokenProvider;
import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AuthService;
import org.tinhpt.digital.service.OAuth2Service;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.ProviderLogin;
import org.tinhpt.digital.utils.GoogleUserInfo;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuth2Service oAuth2Service;

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException{
        try{
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            GoogleUserInfo googleUserInfo = oAuth2Service.extractGoogleUserInfo(oAuth2User);

            User user = userRepository.findByEmail(googleUserInfo.getEmail())
                    .orElseGet(() -> {
                        Role userRole = roleRepository.findByName("USER")
                                .orElseThrow(() -> new RuntimeException("Role not found"));

                        User newUser = User.builder()
                                .email(googleUserInfo.getEmail())
                                .emailVerified(true)
                                .provider(ProviderLogin.GOOGLE.toString())
                                .image(googleUserInfo.getPictureUrl())
                                .roles(Set.of(userRole))
                                .build();
                        return userRepository.save(newUser);
                    });

            if(!googleUserInfo.getPictureUrl().equals(user.getImage())){
                user.setImage(googleUserInfo.getPictureUrl());
                userRepository.save(user);
            }

            TokenPayload payload = TokenPayload.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .permissions(getPermissions(user))
                    .build();

            getRedirectStrategy().sendRedirect(request, response, redirectUri);
        }catch (Exception ex) {
            String failureUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("error", "Authentication failed")
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, failureUrl);
        }
    }

    private Set<PermissionDto> getPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> PermissionDto.builder()
                        .name(permission.getSubject().getSubject())
                        .action(permission.getAction().getAction())
                        .build())
                .collect(Collectors.toSet());
    }
}
