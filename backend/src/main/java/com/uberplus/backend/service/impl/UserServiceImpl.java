package com.uberplus.backend.service.impl;

import com.uberplus.backend.dto.user.UserProfileDTO;
import com.uberplus.backend.dto.user.UserUpdateDTO;
import com.uberplus.backend.model.User;
import com.uberplus.backend.repository.UserRepository;
import com.uberplus.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional()
    public UserProfileDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return new UserProfileDTO(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(String email, UserUpdateDTO update) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (update.getFirstName() != null) {
            user.setFirstName(update.getFirstName().trim());
        }
        if (update.getLastName() != null) {
            user.setLastName(update.getLastName().trim());
        }
        if (update.getPhoneNumber() != null) {
            user.setPhoneNumber(update.getPhoneNumber().trim());
        }
        if (update.getAddress() != null) {
            user.setAddress(update.getAddress().trim());
        }
        if (update.getProfilePicture() != null) {
            user.setProfilePicture(update.getProfilePicture().trim());
        }

        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return new UserProfileDTO(saved);
    }
}
