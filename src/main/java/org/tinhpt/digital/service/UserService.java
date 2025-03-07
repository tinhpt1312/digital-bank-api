package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.request.ChangePassword;
import org.tinhpt.digital.dto.request.UpdateUserRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.UserResponse;

public interface UserService {

    UserResponse getMe(Long userId);

    BankResponse uploadAvatar(Long userId, String fileName);

    UserResponse getUserById(Long id);

    BankResponse updateUser(Long userId, Long id, UpdateUserRequest request);

    BankResponse changePassword(Long userId, ChangePassword request);

}
