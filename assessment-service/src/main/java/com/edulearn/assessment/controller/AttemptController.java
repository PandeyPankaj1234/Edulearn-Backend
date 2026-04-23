package com.edulearn.assessment.controller;

import com.edulearn.assessment.dto.AttemptRequest;
import com.edulearn.assessment.entity.Attempt;
import com.edulearn.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "*")
public class AttemptController {

    @Autowired
    private AssessmentService assessmentService;

    // POST /api/attempts/start
    @PostMapping("/start")
    public ResponseEntity<Attempt> startAttempt(
            @RequestParam Long quizId,
            @RequestParam Long studentId) {
        return ResponseEntity.ok(
                assessmentService.startAttempt(quizId, studentId));
    }

    // POST /api/attempts/submit
    @PostMapping("/submit")
    public ResponseEntity<Attempt> submitAttempt(
            @Valid @RequestBody AttemptRequest request) {
        return ResponseEntity.ok(assessmentService.submitAttempt(request));
    }

    // GET /api/attempts/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attempt>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                assessmentService.getAttemptsByStudent(studentId));
    }

    // GET /api/attempts/quiz/{quizId}
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Attempt>> getByQuiz(
            @PathVariable Long quizId) {
        return ResponseEntity.ok(
                assessmentService.getAttemptsByQuiz(quizId));
    }

    // GET /api/attempts/count
    @GetMapping("/count")
    public ResponseEntity<Long> getAttemptCount(
            @RequestParam Long studentId,
            @RequestParam Long quizId) {
        return ResponseEntity.ok(
                assessmentService.getAttemptCount(studentId, quizId));
    }
}