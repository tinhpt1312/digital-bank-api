package org.tinhpt.digital.service.impl;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tinhpt.digital.config.auth.JwtTokenProvider;
import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.dto.request.LoginRequest;
import org.tinhpt.digital.dto.request.RegisterRequest;
import org.tinhpt.digital.dto.request.VerifyEmailRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.LoginResponse;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.UserCode;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.repository.UserCodeRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AuthService;
import org.tinhpt.digital.service.EmailService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.UserCodeType;
import org.tinhpt.digital.utils.AuthUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserCodeRepository userCodeRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public BankResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.USERNAME_EXISTS_CODE)
                    .responseMessage(AuthUtils.USERNAME_EXISTS_MESSAGE)
                    .build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.EMAIL_EXISTS_CODE)
                    .responseMessage(AuthUtils.EMAIL_EXISTS_MESSAGE)
                    .build();
        }

        Optional<Role> userRole = roleRepository.findByName("USER");
        if (userRole.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.ROLE_NOT_FOUND_CODE)
                    .responseMessage(AuthUtils.ROLE_NOT_FOUND_MESSAGE)
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .emailVerified(false)
                .role(userRole.get())
                .build();

        userRepository.save(user);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, UserCodeType.REGISTER.getExpirationMinutes());

        String verificationCode = generateVerificationCode();
        UserCode userCode = UserCode.builder()
                .code(verificationCode)
                .type(UserCodeType.REGISTER)
                .user(user)
                .expiredAt(calendar.getTime())
                .audit(Audit.builder()
                        .createdBy(user)
                        .build())
                .build();

        userCodeRepository.save(userCode);

//        emailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return BankResponse.builder()
                .responseCode(AuthUtils.SUCCESS_CODE)
                .responseMessage(AuthUtils.SUCCESS_MESSAGE)
                .build();
    }

    @Override
    @Transactional
    public BankResponse verifyEmail(VerifyEmailRequest request) {
        Optional<UserCode> userCodeOpt = userCodeRepository.findByUser_EmailAndCode(
                request.getEmail(),
                request.getCode()
        );

        if (userCodeOpt.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.INVALID_CODE_CODE)
                    .responseMessage(AuthUtils.INVALID_CODE_MESSAGE)
                    .build();
        }

        UserCode userCode = userCodeOpt.get();

        if (userCode.getExpiredAt().before(new Date())) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.ACCOUNT_EXPIRED_CODE)
                    .responseMessage(AuthUtils.ACCOUNT_EXPIRED_MESSAGE)
                    .build();
        }

        if (userCode.getUsedAt() != null) {
            return BankResponse.builder()
                    .responseCode(AuthUtils.ACCOUNT_USED_CODE)
                    .responseMessage(AuthUtils.ACCOUNT_USED_MESSAGE)
                    .build();
        }

        User user = userCode.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        userCode.setUsedAt(new Date());
        userCode.setAudit(Audit.builder()
                        .updatedAt(new Date())
                        .updatedBy(user)
                        .build());
        userCodeRepository.save(userCode);

        return BankResponse.builder()
                .responseCode(AuthUtils.ACCOUNT_VERIFY_CODE)
                .responseMessage(AuthUtils.ACCOUNT_VERIFY_MESSAGE)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!user.isEmailVerified()) {
            return LoginResponse.builder()
                    .accessToken(null)
                    .message("Please check email is verify")
                    .build();
        }

        TokenPayload payload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .permissions(getPermissions(user))
                .build();

        String token = tokenProvider.generateToken(payload);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24*60*60);
        response.addCookie(cookie);

        return LoginResponse.builder()
                .accessToken(token)
                .message("Login is successfully")
                .build();
    }

    @Override
    public BankResponse resendEmailCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.isEmailVerified()) {
            return BankResponse.builder()
                    .responseCode("009")
                    .responseMessage("Email already verified")
                    .build();
        }

        String verificationCode = generateVerificationCode();
        UserCode userCode = UserCode.builder()
                .code(verificationCode)
                .type(UserCodeType.REGISTER)
                .user(user)
                .build();

        userCodeRepository.save(userCode);

//        emailService.sendVerificationEmail(email, verificationCode);

        return BankResponse.builder()
                .responseCode("203")
                .responseMessage("Code are send to Email is successfully! Please check email")
                .build();
    }

    @Override
    public BankResponse logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Logout is successfully")
                .build();
    }


    private Set<PermissionDto> getPermissions(User user) {
        if (user.getRole() == null) {
            return Collections.emptySet();
        }

        return user.getRole().getPermissions().stream()
                .map(permission -> PermissionDto.builder()
                        .name(permission.getSubject().toString())
                        .action(permission.getAction().toString())
                        .build())
                .collect(Collectors.toSet());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
