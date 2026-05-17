package com.edulearn.notification.controller;

import com.edulearn.notification.dto.BulkNotificationRequest;
import com.edulearn.notification.dto.NotificationRequest;
import com.edulearn.notification.entity.Notification;
import com.edulearn.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // POST /api/notifications/send
    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(
                notificationService.sendNotification(request));
    }

    // POST /api/notifications/send-bulk
    @PostMapping("/send-bulk")
    public ResponseEntity<List<Notification>> sendBulk(
            @Valid @RequestBody BulkNotificationRequest request) {
        return ResponseEntity.ok(
                notificationService.sendBulkNotification(request));
    }

    // GET /api/notifications/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationService.getByUser(userId));
    }

    // GET /api/notifications/user/{userId}/unread
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnread(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationService.getUnreadByUser(userId));
    }

    // GET /api/notifications/user/{userId}/count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId));
    }

    // PUT /api/notifications/{notificationId}/read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read!");
    }

    // PUT /api/notifications/user/{userId}/read-all
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllRead(
            @PathVariable Long userId) {
        notificationService.markAllRead(userId);
        return ResponseEntity.ok("All notifications marked as read!");
    }

    // DELETE /api/notifications/{notificationId}
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted!");
    }

    // GET /api/notifications/all
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(
                notificationService.getAllNotifications());
    }

    // POST /api/notifications/email
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String toEmail,
            @RequestParam String subject,
            @RequestParam String body) {
        notificationService.sendEmailAlert(toEmail, subject, body);
        return ResponseEntity.ok("Email sent successfully!");
    }
}