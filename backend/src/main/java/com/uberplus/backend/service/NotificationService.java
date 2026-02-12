package com.uberplus.backend.service;

import com.uberplus.backend.dto.notification.NotificationDTO;
import com.uberplus.backend.model.Ride;
import com.uberplus.backend.model.User;
import com.uberplus.backend.model.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    void notifyRideAccepted(Ride ride);
    void notifyRideCompleted(Ride ride);
    void sendNotificationToUser(User user, NotificationType type, String message, Ride ride);
    List<NotificationDTO> getUserNotifications(Integer userId);
    void markAsRead(Integer userId, Integer notificationId);
    Long getUnreadCount(Integer userId);
}