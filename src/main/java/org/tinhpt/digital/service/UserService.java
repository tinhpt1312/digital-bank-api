package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.response.UserResponse;

public interface UserService {

    UserResponse getMe(Long userId);

}
