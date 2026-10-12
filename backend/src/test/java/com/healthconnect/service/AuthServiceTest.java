package com.healthconnect.service;

import com.healthconnect.dto.AuthRequest;
import com.healthconnect.dto.AuthResponse;
import com.healthconnect.dto.RegisterRequest;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.User;
import com.healthconnect.exception.BadRequestException;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register User: Successfully creates new patient account")
    void testRegisterUserSuccess() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123", "+1234567890", Role.ROLE_PATIENT);

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });
        when(jwtUtils.generateTokenFromEmail("john@example.com")).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("john@example.com", response.getUser().getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Register User: Fails when email is already registered")
    void testRegisterUserDuplicateEmail() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123", "+1234567890", Role.ROLE_PATIENT);

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("Login User: Successful authentication returns JWT token")
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest("john@example.com", "password123");
        User user = new User(10L, "John Doe", "john@example.com", "encodedPassword", "+1234567890", Role.ROLE_PATIENT, true);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken(auth)).thenReturn("mock-jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("john@example.com", response.getUser().getEmail());
    }
}
