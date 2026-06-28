package com.rm.service;

import com.rm.dto.AuthResponse;
import com.rm.dto.ChangePasswordRequest;
import com.rm.dto.LoginRequest;
import com.rm.dto.RegisterRequest;
import com.rm.entity.Role;
import com.rm.repository.UserRepository;
import com.rm.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rm.entity.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    private final AuditService auditService;

    public String register(RegisterRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        return "User Registered Successfully";
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow();

        String token =
                jwtService.generateToken(userDetails);

        auditService.log(
                user.getEmail(),
                "LOGIN",
                "User",
                user.getId(),
                "User logged in"
        );

        return AuthResponse.builder()
                .token(token)
                .firstLogin(user.getFirstLogin())
                .build();
    }

    @Transactional
    public String changePassword(
            ChangePasswordRequest request,
            Authentication authentication
    ) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow();

        if(!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Old password incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setFirstLogin(false);

        userRepository.save(user);

        return "Password Changed Successfully";
    }
}
