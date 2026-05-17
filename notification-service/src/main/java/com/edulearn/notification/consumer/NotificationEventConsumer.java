package com.edulearn.notification.consumer;

import com.edulearn.notification.dto.NotificationEvent;
import com.edulearn.notification.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.edulearn.notification.config.RabbitMQConfig.QUEUE;

@Slf4j
@Component
public class NotificationEventConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = QUEUE)
    public void handleEvent(NotificationEvent event) {
        log.info("📬 Received event: {} for {}", event.getEventType(), event.getRecipientEmail());

        try {
            switch (event.getEventType()) {

                // ── Enrollment ───────────────────────────────────────────────
                case "ENROLLMENT_CREATED" -> {
                    // Email to student
                    emailService.sendEnrollmentConfirmation(
                            event.getRecipientEmail(),
                            event.getRecipientName(),
                            event.getCourseName());
                    // Email to instructor
                    if (event.getInstructorEmail() != null) {
                        emailService.sendNewStudentAlert(
                                event.getInstructorEmail(),
                                event.getInstructorName(),
                                event.getRecipientName(),
                                event.getCourseName());
                    }
                }

                // ── Payment ──────────────────────────────────────────────────
                case "PAYMENT_COMPLETED" -> emailService.sendPaymentReceipt(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getCourseName(),
                        event.getAmount());

                // ── Subscription ─────────────────────────────────────────────
                case "SUBSCRIPTION_CREATED" -> emailService.sendSubscriptionConfirmation(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getAmount());

                case "SUBSCRIPTION_REFUNDED" -> emailService.sendRefundNotice(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getAmount());

                // ── Course lifecycle (admin / instructor) ────────────────────
                case "COURSE_SUBMITTED" -> emailService.sendCourseSubmittedToAdmin(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getCourseName());

                case "COURSE_APPROVED" -> emailService.sendCourseApprovedToInstructor(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getCourseName());

                case "COURSE_REJECTED" -> emailService.sendCourseRejectedToInstructor(
                        event.getRecipientEmail(),
                        event.getRecipientName(),
                        event.getCourseName(),
                        event.getRejectionReason());

                default -> log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception ex) {
            log.error("Failed to process notification event {}: {}", event.getEventType(), ex.getMessage());
        }
    }
}
