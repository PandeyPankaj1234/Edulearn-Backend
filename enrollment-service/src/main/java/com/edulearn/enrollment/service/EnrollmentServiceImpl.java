package com.edulearn.enrollment.service;

import com.edulearn.enrollment.dto.EnrollmentRequest;
import com.edulearn.enrollment.entity.Enrollment;
import com.edulearn.enrollment.messaging.EventPublisher;
import com.edulearn.enrollment.messaging.NotificationEvent;
import com.edulearn.enrollment.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public Enrollment enroll(EnrollmentRequest request) {
        // Already enrolled check
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.getStudentId(), request.getCourseId())) {
            throw new RuntimeException("Student already enrolled in this course!");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(request.getStudentId());
        enrollment.setCourseId(request.getCourseId());
        Enrollment saved = enrollmentRepository.save(enrollment);

        // Publish ENROLLMENT_CREATED event → notification-service sends emails
        if (request.getStudentEmail() != null) {
            NotificationEvent event = new NotificationEvent();
            event.setEventType("ENROLLMENT_CREATED");
            event.setRecipientEmail(request.getStudentEmail());
            event.setRecipientName(request.getStudentName() != null ? request.getStudentName() : "Student");
            event.setCourseName(request.getCourseName());
            event.setInstructorEmail(request.getInstructorEmail());
            event.setInstructorName(request.getInstructorName());
            event.setRelatedEntityId(saved.getEnrollmentId());
            event.setRelatedEntityType("ENROLLMENT");
            event.setEventTime(LocalDateTime.now());
            eventPublisher.publish("notification.enrollment.created", event);
        }
        return saved;
    }

    @Override
    public void unenroll(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));
        enrollment.setStatus("Cancelled");
        enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    public void updateProgress(Long enrollmentId, Integer progressPercent) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));
        enrollment.setProgressPercent(progressPercent);

        // Auto complete if 100%
        if (progressPercent >= 100) {
            enrollment.setStatus("Completed");
            enrollment.setCompletedAt(LocalDate.now());
        }
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void markComplete(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));
        enrollment.setStatus("Completed");
        enrollment.setProgressPercent(100);
        enrollment.setCompletedAt(LocalDate.now());
        enrollmentRepository.save(enrollment);
    }

    @Override
    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public void issueCertificate(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));

        if (!enrollment.getStatus().equals("Completed")) {
            throw new RuntimeException("Course not completed yet!");
        }
        enrollment.setCertificateIssued(true);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public long getEnrollmentCount(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}