package com.edulearn.enrollment.controller;

import com.edulearn.enrollment.dto.EnrollmentRequest;
import com.edulearn.enrollment.entity.Enrollment;
import com.edulearn.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // POST /api/enrollments
    @PostMapping
    public ResponseEntity<Enrollment> enroll(
            @Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(enrollmentService.enroll(request));
    }

    // PUT /api/enrollments/{enrollmentId}/unenroll
    @PutMapping("/{enrollmentId}/unenroll")
    public ResponseEntity<String> unenroll(
            @PathVariable Long enrollmentId) {
        enrollmentService.unenroll(enrollmentId);
        return ResponseEntity.ok("Unenrolled successfully!");
    }

    // GET /api/enrollments/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    // GET /api/enrollments/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    // PUT /api/enrollments/{enrollmentId}/progress
    @PutMapping("/{enrollmentId}/progress")
    public ResponseEntity<String> updateProgress(
            @PathVariable Long enrollmentId,
            @RequestParam Integer progressPercent) {
        enrollmentService.updateProgress(enrollmentId, progressPercent);
        return ResponseEntity.ok("Progress updated to " + progressPercent + "%");
    }

    // PUT /api/enrollments/{enrollmentId}/complete
    @PutMapping("/{enrollmentId}/complete")
    public ResponseEntity<String> markComplete(
            @PathVariable Long enrollmentId) {
        enrollmentService.markComplete(enrollmentId);
        return ResponseEntity.ok("Course marked as completed!");
    }

    // GET /api/enrollments/check
    @GetMapping("/check")
    public ResponseEntity<Boolean> isEnrolled(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.isEnrolled(studentId, courseId));
    }

    // POST /api/enrollments/{enrollmentId}/certificate
    @PostMapping("/{enrollmentId}/certificate")
    public ResponseEntity<String> issueCertificate(
            @PathVariable Long enrollmentId) {
        enrollmentService.issueCertificate(enrollmentId);
        return ResponseEntity.ok("Certificate issued successfully!");
    }

    // GET /api/enrollments/course/{courseId}/count
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> getEnrollmentCount(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentCount(courseId));
    }
}