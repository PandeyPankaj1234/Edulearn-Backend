package com.edulearn.notification.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ── Core send method ────────────────────────────────────────────────────

    public void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail, "EduLearn Platform");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML
            mailSender.send(msg);
            log.info("✉️  Email sent to {} — {}", to, subject);
        } catch (Exception e) {
            log.error("Email failed to {}: {}", to, e.getMessage());
        }
    }

    // ── Enrollment ──────────────────────────────────────────────────────────

    public void sendEnrollmentConfirmation(String email, String name, String courseName) {
        send(email, "🎓 Enrollment Confirmed — " + courseName,
            html(name,
                "Enrollment Confirmed!",
                "You have successfully enrolled in <strong>" + courseName + "</strong>.",
                "Start learning now and track your progress on your dashboard.",
                "#4f46e5", "🎓"));
    }

    public void sendNewStudentAlert(String email, String instructorName,
                                    String studentName, String courseName) {
        send(email, "📢 New Student Enrolled in " + courseName,
            html(instructorName,
                "New Student Enrolled",
                "<strong>" + studentName + "</strong> has enrolled in your course <strong>"
                    + courseName + "</strong>.",
                "Log in to your instructor dashboard to track their progress.",
                "#0ea5e9", "📢"));
    }

    // ── Payment ─────────────────────────────────────────────────────────────

    public void sendPaymentReceipt(String email, String name, String courseName, Double amount) {
        String amt = amount != null ? String.format("$%.2f", amount) : "N/A";
        send(email, "✅ Payment Receipt — " + courseName,
            html(name,
                "Payment Successful",
                "Your payment of <strong>" + amt + "</strong> for <strong>" + courseName
                    + "</strong> was processed successfully.",
                "Your course access has been activated. Happy learning!",
                "#16a34a", "✅"));
    }

    // ── Subscription ────────────────────────────────────────────────────────

    public void sendSubscriptionConfirmation(String email, String name, Double amount) {
        String amt = amount != null ? String.format("$%.2f", amount) : "N/A";
        send(email, "🌟 Subscription Activated — EduLearn",
            html(name,
                "Subscription Confirmed",
                "Your EduLearn subscription has been activated for <strong>" + amt + "</strong>.",
                "You now have unlimited access to all platform courses. Enjoy learning!",
                "#7c3aed", "🌟"));
    }

    public void sendRefundNotice(String email, String name, Double amount) {
        String amt = amount != null ? String.format("$%.2f", amount) : "N/A";
        send(email, "💳 Refund Processed — EduLearn",
            html(name,
                "Refund Processed",
                "Your subscription refund of <strong>" + amt + "</strong> has been processed.",
                "The amount will appear in your account within 5–7 business days.",
                "#f59e0b", "💳"));
    }

    // ── Course lifecycle ────────────────────────────────────────────────────

    public void sendCourseSubmittedToAdmin(String email, String adminName, String courseName) {
        send(email, "📋 Course Pending Review — " + courseName,
            html(adminName,
                "New Course Awaiting Review",
                "A new course <strong>" + courseName + "</strong> has been submitted for review.",
                "Please log in to your admin dashboard to approve or reject it.",
                "#dc2626", "📋"));
    }

    public void sendCourseApprovedToInstructor(String email, String name, String courseName) {
        send(email, "✅ Course Approved — " + courseName,
            html(name,
                "Your Course Was Approved!",
                "Congratulations! Your course <strong>" + courseName
                    + "</strong> has been approved and is now live.",
                "Students can now find and enroll in your course. Great work!",
                "#16a34a", "✅"));
    }

    public void sendCourseRejectedToInstructor(String email, String name,
                                                String courseName, String reason) {
        send(email, "❌ Course Needs Revision — " + courseName,
            html(name,
                "Course Revision Needed",
                "Your course <strong>" + courseName + "</strong> requires some changes before approval.",
                "<strong>Admin feedback:</strong> " + (reason != null ? reason : "Please review guidelines."),
                "#dc2626", "❌"));
    }

    // ── Admin broadcast ─────────────────────────────────────────────────────

    public void sendAdminBroadcast(String email, String type, String title, String message) {
        String color = switch (type != null ? type : "") {
            case "MAINTENANCE" -> "#f59e0b";
            case "PROMOTION"   -> "#7c3aed";
            case "REMINDER"    -> "#0ea5e9";
            case "COURSE_UPDATE" -> "#16a34a";
            default            -> "#4f46e5"; // ANNOUNCEMENT
        };
        String emoji = switch (type != null ? type : "") {
            case "MAINTENANCE" -> "🔧";
            case "PROMOTION"   -> "🎁";
            case "REMINDER"    -> "⏰";
            case "COURSE_UPDATE" -> "📚";
            default            -> "📢";
        };
        send(email, emoji + " " + title + " — EduLearn",
            html("there",
                title,
                message,
                "This is an official notification from the EduLearn administration team.",
                color, emoji));
    }

    // ── HTML template ───────────────────────────────────────────────────────

    private String html(String name, String heading, String body,
                        String footer, String color, String emoji) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;padding:0;background:#f1f5f9;font-family:Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td align="center" style="padding:32px 16px;">
                  <table width="560" cellpadding="0" cellspacing="0"
                         style="background:#ffffff;border-radius:12px;overflow:hidden;
                                box-shadow:0 4px 24px rgba(0,0,0,0.08);">
                    <!-- Header -->
                    <tr>
                      <td style="background:%s;padding:28px 32px;text-align:center;">
                        <div style="font-size:36px;">%s</div>
                        <h1 style="color:#ffffff;margin:8px 0 0;font-size:22px;">%s</h1>
                      </td>
                    </tr>
                    <!-- Body -->
                    <tr>
                      <td style="padding:32px;">
                        <p style="color:#374151;font-size:15px;margin:0 0 16px;">Hi <strong>%s</strong>,</p>
                        <p style="color:#374151;font-size:15px;line-height:1.6;margin:0 0 16px;">%s</p>
                        <p style="color:#6b7280;font-size:13px;margin:0;">%s</p>
                      </td>
                    </tr>
                    <!-- Footer -->
                    <tr>
                      <td style="background:#f8fafc;padding:20px 32px;text-align:center;
                                 border-top:1px solid #e2e8f0;">
                        <p style="color:#9ca3af;font-size:12px;margin:0;">
                          EduLearn Platform · You received this because you have an account with us.
                        </p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(color, emoji, heading, name, body, footer);
    }
}
