package com.healthconnect.controller;

import com.healthconnect.dto.UserDto;
import com.healthconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(Authentication authentication, @RequestBody Map<String, String> body) {
        String email = authentication.getName();
        String fullName = body.get("fullName");
        String phone = body.get("phone");
        return ResponseEntity.ok(userService.updateProfile(email, fullName, phone));
    }
}
