package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.config.auth.JwtTokenProvider;
import org.tinhpt.digital.dto.request.LoginRequest;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.LoginResponse;
import org.tinhpt.digital.dto.response.VerifyEmailResponse;
import org.tinhpt.digital.service.AuthService;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Api authentication")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public BankResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/verify")
    public BankResponse verifyEmail(@RequestBody VerifyEmailRequest request) {
        return authService.verifyEmail(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/resend-code")
    public BankResponse resendCode(@RequestBody String email){
        return authService.resendEmailCode(email);
    }

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}
