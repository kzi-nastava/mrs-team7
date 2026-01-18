package com.uberplus.backend.dto.user;

import com.uberplus.backend.model.User;
import com.uberplus.backend.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserProfileDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String profilePicture;
    private UserRole role;
    private boolean blocked;
    private String blockReason;
    private boolean activated;

    public UserProfileDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        profilePicture = user.getProfilePicture();
        role = user.getRole();
        blocked = user.isBlocked();
        blockReason = user.getBlockReason();
        activated = user.isActivated();
    }
}
