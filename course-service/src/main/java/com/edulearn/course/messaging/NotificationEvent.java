package com.edulearn.course.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {
    private String eventType;
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private String courseName;
    private String instructorEmail;
    private String instructorName;
    private Double amount;
    private String rejectionReason;
    private Long   relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime eventTime;

    public NotificationEvent(String eventType, String recipientEmail,
                             String recipientName, String subject, String message) {
        this.eventType      = eventType;
        this.recipientEmail = recipientEmail;
        this.recipientName  = recipientName;
        this.subject        = subject;
        this.message        = message;
        this.eventTime      = LocalDateTime.now();
    }
}
