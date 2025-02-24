package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;

public interface AuthService {
    BankResponse register(RegisterRequest request);

    BankResponse verifyEmail(VerifyEmailRequest request);
}
