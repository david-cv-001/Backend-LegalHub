package com.unsis.legalhub.auth;

import com.unsis.legalhub.auth.dto.LoginRequest;
import com.unsis.legalhub.auth.dto.LoginResponse;
import com.unsis.legalhub.common.exception.InvalidCredentialsException;
import com.unsis.legalhub.security.JwtProperties;
import com.unsis.legalhub.security.JwtService;
import com.unsis.legalhub.superadmin.Superadmin;
import com.unsis.legalhub.superadmin.SuperadminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String ROLE = "ROLE_SUPERADMIN";

    private final SuperadminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(
            SuperadminRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public LoginResponse login(LoginRequest request) {
        Superadmin admin = repository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return new LoginResponse(
                jwtService.generateToken(admin.getUsername(), ROLE),
                "Bearer",
                (int) jwtProperties.getExpirationSeconds(),
                admin.getUsername(),
                ROLE
        );
    }
}
