package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.request.ChangePassword;
import org.tinhpt.digital.dto.request.UpdateUserRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.UserResponse;
import org.tinhpt.digital.service.UserService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

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

    @GetMapping("/{id}")
    @RequirePermission(subject = SubjectName.USER, action = PermissionsAction.READ)
    public UserResponse getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @PatchMapping("/profile/avatar")
    public BankResponse updatedAvatar(@CurrentUser TokenPayload tokenPayload, @RequestParam String fileName){
        Long userId = tokenPayload.getUserId();

        return userService.uploadAvatar(userId, fileName);
    }

    @PatchMapping("/{id}")
    @RequirePermission(subject = SubjectName.USER, action = PermissionsAction.UPDATE)
    public BankResponse updatedUser(@PathVariable Long id,@CurrentUser TokenPayload tokenPayload, @RequestBody UpdateUserRequest request){
        Long userId = tokenPayload.getUserId();

        return userService.updateUser(userId, id, request);
    }

    @PatchMapping("/change-password")
    public BankResponse changePassword(@CurrentUser TokenPayload tokenPayload, @RequestBody ChangePassword request){
        Long userId = tokenPayload.getUserId();

        return userService.changePassword(userId, request);
    }
}
