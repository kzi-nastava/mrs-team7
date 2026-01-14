package com.uberplus.backend.controller;

import com.uberplus.backend.dto.user.UserProfileDTO;
import com.uberplus.backend.dto.user.UserUpdateDTO;
import com.uberplus.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users/profile
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        UserProfileDTO profile = userService.getByEmail(email);
        return ResponseEntity.ok(profile);
    }

    // PUT /api/users/profile
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(Authentication authentication, @Valid @RequestBody UserUpdateDTO update) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserProfileDTO saved = userService.updateProfile(authentication.getName(), update);
        return ResponseEntity.ok(saved);
    }
}