package org.tinhpt.digital.service;

import org.apache.coyote.BadRequestException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.tinhpt.digital.dto.request.LoginRequest;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.LoginResponse;
import org.tinhpt.digital.utils.GoogleUserInfo;

public interface AuthService {
    BankResponse register(RegisterRequest request);

    BankResponse verifyEmail(VerifyEmailRequest request);
    LoginResponse login(LoginRequest request);

    BankResponse resendEmailCode(String email);

}
