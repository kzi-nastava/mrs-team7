package com.uberplus.backend.service;

import com.uberplus.backend.dto.user.UserProfileDTO;
import com.uberplus.backend.dto.user.UserUpdateDTO;

public interface UserService {
    UserProfileDTO getByEmail(String email);
    UserProfileDTO updateProfile(String email, UserUpdateDTO update);
}
