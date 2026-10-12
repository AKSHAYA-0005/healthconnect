package com.healthconnect.service;

import com.healthconnect.dto.UserDto;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.User;
import com.healthconnect.exception.ResourceNotFoundException;
import com.healthconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return new UserDto(user);
    }

    public UserDto updateUserStatus(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEnabled(enabled);
        User updated = userRepository.save(user);
        return new UserDto(updated);
    }

    public UserDto updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setRole(role);
        User updated = userRepository.save(user);
        return new UserDto(updated);
    }

    public UserDto updateProfile(String email, String fullName, String phone) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        User updated = userRepository.save(user);
        return new UserDto(updated);
    }
}
