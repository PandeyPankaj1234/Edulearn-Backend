package com.edulearn.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Universal event object published to RabbitMQ by all EduLearn services.
 * The notification-service consumes this and sends real emails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {

    private String eventType;       // e.g. "ENROLLMENT_CREATED", "PAYMENT_COMPLETED"
    private String recipientEmail;  // real email to send to
    private String recipientName;
    private String subject;         // email subject
    private String message;         // email body (plain text fallback)

    // Optional context fields
    private String courseName;
    private String instructorEmail; // if both student + instructor need notifying
    private String instructorName;
    private Double amount;
    private String rejectionReason;
    private Long   relatedEntityId;
    private String relatedEntityType; // "COURSE", "ENROLLMENT", "PAYMENT", "SUBSCRIPTION"

    private LocalDateTime eventTime = LocalDateTime.now();

    // Convenience constructor for simple notifications
    public NotificationEvent(String eventType, String recipientEmail,
                             String recipientName, String subject, String message) {
        this.eventType     = eventType;
        this.recipientEmail = recipientEmail;
        this.recipientName  = recipientName;
        this.subject        = subject;
        this.message        = message;
        this.eventTime      = LocalDateTime.now();
    }
}
