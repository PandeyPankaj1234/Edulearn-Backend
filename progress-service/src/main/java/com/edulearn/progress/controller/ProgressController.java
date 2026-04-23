package com.edulearn.progress.controller;

import com.edulearn.progress.dto.CertificateResponse;
import com.edulearn.progress.dto.ProgressRequest;
import com.edulearn.progress.entity.Certificate;
import com.edulearn.progress.entity.Progress;
import com.edulearn.progress.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    // POST /api/progress/track
    @PostMapping("/track")
    public ResponseEntity<Progress> trackProgress(
            @Valid @RequestBody ProgressRequest request) {
        return ResponseEntity.ok(
                progressService.trackProgress(request));
    }

    // PUT /api/progress/complete
    @PutMapping("/complete")
    public ResponseEntity<Progress> markComplete(
            @RequestParam Long studentId,
            @RequestParam Long lessonId) {
        return ResponseEntity.ok(
                progressService.markLessonComplete(studentId, lessonId));
    }

    // GET /api/progress/course
    @GetMapping("/course")
    public ResponseEntity<Integer> getCourseProgress(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam int totalLessons) {
        return ResponseEntity.ok(
                progressService.getCourseProgress(
                        studentId, courseId, totalLessons));
    }

    // GET /api/progress/lesson
    @GetMapping("/lesson")
    public ResponseEntity<Optional<Progress>> getLessonProgress(
            @RequestParam Long studentId,
            @RequestParam Long lessonId) {
        return ResponseEntity.ok(
                progressService.getLessonProgress(studentId, lessonId));
    }

    // GET /api/progress/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Progress>> getAllProgress(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                progressService.getAllProgressByStudent(studentId));
    }

    // POST /api/progress/certificates/issue
    @PostMapping("/certificates/issue")
    public ResponseEntity<Certificate> issueCertificate(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam String studentName,
            @RequestParam String courseName,
            @RequestParam String instructorName) {
        return ResponseEntity.ok(
                progressService.issueCertificate(
                        studentId, courseId,
                        studentName, courseName, instructorName));
    }

    // GET /api/progress/certificates
    @GetMapping("/certificates")
    public ResponseEntity<CertificateResponse> getCertificate(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(
                progressService.getCertificate(studentId, courseId));
    }

    // GET /api/progress/certificates/verify/{code}
    @GetMapping("/certificates/verify/{code}")
    public ResponseEntity<Certificate> verifyCertificate(
            @PathVariable String code) {
        return ResponseEntity.ok(
                progressService.verifyCertificate(code));
    }

    // GET /api/progress/certificates/student/{studentId}
    @GetMapping("/certificates/student/{studentId}")
    public ResponseEntity<List<Certificate>> getCertificatesByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                progressService.getCertificatesByStudent(studentId));
    }
}