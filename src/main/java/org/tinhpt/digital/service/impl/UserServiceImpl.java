package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.response.RoleResponse;
import org.tinhpt.digital.dto.response.UserResponse;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getMe(Long userId) {
        Optional<User> optionalUser = userRepository.findUserWithRoleById(userId);
        User user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .image(user.getImage())
                .provider(user.getProvider())
                .role(RoleResponse.builder()
                        .id(user.getRole().getId())
                        .name(user.getRole().getName())
                        .build())
                .isPassword(user.getPassword() != null)
                .build();
    }
}
