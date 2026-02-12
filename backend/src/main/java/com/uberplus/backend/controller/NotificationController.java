package com.uberplus.backend.controller;

import com.uberplus.backend.dto.notification.NotificationDTO;
import com.uberplus.backend.model.User;
import com.uberplus.backend.repository.UserRepository;
import com.uberplus.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // GET /api/notifications
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }

    // PUT /api/notifications/{id}/read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer notificationId,
            Authentication auth
    ) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.markAsRead(user.getId(), notificationId);
        return ResponseEntity.ok().build();
    }

    // GET /api/notifications/unread-count
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(notificationService.getUnreadCount(user.getId()));
    }
}