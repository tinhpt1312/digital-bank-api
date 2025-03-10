package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.dto.request.LoginRequest;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.LoginResponse;
import org.tinhpt.digital.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Api authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public BankResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/verify")
    public BankResponse verifyEmail(@RequestBody VerifyEmailRequest request) {
        return authService.verifyEmail(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response){
        return authService.login(request, response);
    }

    @PostMapping("/logout")
    public BankResponse logout(HttpServletResponse response){
        return authService.logout(response);
    }

    @PostMapping("/resend-code")
    public BankResponse resendCode(@RequestBody String email){
        return authService.resendEmailCode(email);
    }

}
