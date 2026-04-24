package com.edulearn.notification.service;

import com.edulearn.notification.dto.BulkNotificationRequest;
import com.edulearn.notification.dto.NotificationRequest;
import com.edulearn.notification.entity.Notification;
import com.edulearn.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public Notification sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRelatedEntityId(request.getRelatedEntityId());
        notification.setRelatedEntityType(
                request.getRelatedEntityType());
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> sendBulkNotification(
            BulkNotificationRequest request) {
        List<Notification> notifications = new ArrayList<>();
        for (Long userId : request.getUserIds()) {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setType(request.getType());
            notification.setTitle(request.getTitle());
            notification.setMessage(request.getMessage());
            notification.setRelatedEntityId(
                    request.getRelatedEntityId());
            notification.setRelatedEntityType(
                    request.getRelatedEntityType());
            notification.setIsRead(false);
            notifications.add(notification);
        }
        return notificationRepository.saveAll(notifications);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException(
                        "Notification not found!"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllRead(Long userId) {
        List<Notification> unread = notificationRepository
                .findByUserIdAndIsRead(userId, false);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }

    @Override
    public List<Notification> getByUser(Long userId) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getUnreadByUser(Long userId) {
        return notificationRepository
                .findByUserIdAndIsRead(userId, false);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository
                .countByUserIdAndIsRead(userId, false);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public void sendEmailAlert(String toEmail,
                               String subject, String body) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured — " +
                    "skipping email to: " + toEmail);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }
}