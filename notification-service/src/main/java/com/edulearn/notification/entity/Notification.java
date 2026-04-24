package com.edulearn.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private Long userId;

    private String type;
    // ENROLLMENT, PAYMENT, QUIZ_RESULT,
    // CERTIFICATE, COURSE_PUBLISHED

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String message;

    private Boolean isRead = false;

    private LocalDateTime createdAt;

    private Long relatedEntityId;

    private String relatedEntityType;
    // course, quiz, payment, certificate

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isRead == null) {
            this.isRead = false;
        }
    }
}