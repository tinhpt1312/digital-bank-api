package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.VerifyEmailResponse;
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
}
