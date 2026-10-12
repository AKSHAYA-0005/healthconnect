package com.healthconnect.service;

import com.healthconnect.dto.AuthRequest;
import com.healthconnect.dto.AuthResponse;
import com.healthconnect.dto.RegisterRequest;
import com.healthconnect.dto.UserDto;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.User;
import com.healthconnect.exception.BadRequestException;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("Account is disabled. Please contact administrator.");
        }

        return new AuthResponse(jwt, new UserDto(user));
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? request.getRole() : Role.ROLE_PATIENT);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        String jwt = jwtUtils.generateTokenFromEmail(savedUser.getEmail());
        return new AuthResponse(jwt, new UserDto(savedUser));
    }

    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));
        return new UserDto(user);
    }
}
