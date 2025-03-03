package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.dto.request.LoginRequest;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.LoginResponse;
import org.tinhpt.digital.service.AuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<String> logout(HttpServletResponse response){
        return authService.logout(response);
    }

    @PostMapping("/resend-code")
    public BankResponse resendCode(@RequestBody String email){
        return authService.resendEmailCode(email);
    }

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/google/login-success")
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/google/login-failure")
    public ResponseEntity<Map<String, String>> loginFailure(HttpServletRequest request) {
        System.out.println("Google login failure endpoint called" + request);

        Exception exception = (Exception) request.getSession().getAttribute("error");
        String errorMessage = exception != null ? exception.getMessage() : "Unknown error occurred";
        System.out.println("Google login error details: "+ exception);

        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Failed to login with Google: " + errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
