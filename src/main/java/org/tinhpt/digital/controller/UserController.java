package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.dto.response.UserResponse;
import org.tinhpt.digital.service.UserService;
import org.tinhpt.digital.share.TokenPayload;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Api User")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getMe(@CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();
        return userService.getMe(userId);
    }
}
