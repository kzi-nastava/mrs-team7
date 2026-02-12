package com.uberplus.backend.service.impl;

import com.uberplus.backend.dto.notification.NotificationDTO;
import com.uberplus.backend.model.Notification;
import com.uberplus.backend.model.Passenger;
import com.uberplus.backend.model.Ride;
import com.uberplus.backend.model.User;
import com.uberplus.backend.model.enums.NotificationType;
import com.uberplus.backend.repository.NotificationRepository;
import com.uberplus.backend.service.EmailService;
import com.uberplus.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void notifyRideAccepted(Ride ride) {
        // Notifikuj sve putnike
        for (Passenger passenger : ride.getPassengers()) {
            // Samo linked passengers, ne creator
            if (!passenger.getId().equals(ride.getCreator().getId())) {
                String message = String.format(
                        "You've been added to a ride to %s scheduled for %s",
                        ride.getEndLocation().getAddress(),
                        ride.getScheduledTime().toString()
                );

                sendNotificationToUser(passenger, NotificationType.RIDE_ACCEPTED, message, ride);

                // Posalji email
                emailService.sendRideAcceptedEmail(ride, passenger);
            }
        }
    }

    @Override
    @Transactional
    public void notifyRideCompleted(Ride ride) {
        // Notifikuj SVE putnike (i creator i linked)
        for (Passenger passenger : ride.getPassengers()) {
            String message = String.format(
                    "Your ride to %s has been completed. Total cost: €%.2f",
                    ride.getEndLocation().getAddress(),
                    ride.getTotalPrice()
            );

            sendNotificationToUser(passenger, NotificationType.RIDE_COMPLETED, message, ride);

            // Posalji email
            emailService.sendRideCompletedEmail(ride, passenger);
        }
    }

    @Override
    @Transactional
    public void sendNotificationToUser(User user, NotificationType type, String message, Ride ride) {
        // Kreiraj notifikaciju u bazi
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRide(ride);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        // Posalji preko WebSocket-a
        NotificationDTO dto = new NotificationDTO(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getRide() != null ? notification.getRide().getId() : null,
                notification.isRead(),
                notification.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), dto);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(n -> new NotificationDTO(
                        n.getId(),
                        n.getType(),
                        n.getMessage(),
                        n.getRide() != null ? n.getRide().getId() : null,
                        n.isRead(),
                        n.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Integer userId, Integer notificationId) {
        notificationRepository.markAsRead(userId, notificationId);
    }

    @Override
    public Long getUnreadCount(Integer userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
}