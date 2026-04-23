package com.edulearn.enrollment.service;

import com.edulearn.enrollment.dto.EnrollmentRequest;
import com.edulearn.enrollment.entity.Enrollment;
import com.edulearn.enrollment.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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
        return enrollmentRepository.save(enrollment);
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
}