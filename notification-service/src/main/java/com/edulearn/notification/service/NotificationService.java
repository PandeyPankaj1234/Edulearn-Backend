package com.edulearn.notification.service;

import com.edulearn.notification.dto.BulkNotificationRequest;
import com.edulearn.notification.dto.NotificationRequest;
import com.edulearn.notification.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification sendNotification(NotificationRequest request);

    List<Notification> sendBulkNotification(
            BulkNotificationRequest request);

    void markAsRead(Long notificationId);

    void markAllRead(Long userId);

    List<Notification> getByUser(Long userId);

    List<Notification> getUnreadByUser(Long userId);

    long getUnreadCount(Long userId);

    void deleteNotification(Long notificationId);

    List<Notification> getAllNotifications();

    void sendEmailAlert(String toEmail,
                        String subject, String body);
}