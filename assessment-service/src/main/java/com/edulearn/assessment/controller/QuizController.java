package com.edulearn.assessment.controller;

import com.edulearn.assessment.dto.QuestionRequest;
import com.edulearn.assessment.dto.QuizRequest;
import com.edulearn.assessment.entity.Question;
import com.edulearn.assessment.entity.Quiz;
import com.edulearn.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private AssessmentService assessmentService;

    // POST /api/quizzes
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(assessmentService.createQuiz(request));
    }

    // POST /api/quizzes/questions
    @PostMapping("/questions")
    public ResponseEntity<Question> addQuestion(
            @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(assessmentService.addQuestion(request));
    }

    // GET /api/quizzes/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getQuizzesByCourse(courseId));
    }

    // GET /api/quizzes/{quizId}/questions
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestions(
            @PathVariable Long quizId) {
        return ResponseEntity.ok(assessmentService.getQuestionsByQuiz(quizId));
    }

    // PUT /api/quizzes/{quizId}
    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(assessmentService.updateQuiz(quizId, request));
    }

    // PUT /api/quizzes/{quizId}/publish
    @PutMapping("/{quizId}/publish")
    public ResponseEntity<String> publishQuiz(
            @PathVariable Long quizId) {
        assessmentService.publishQuiz(quizId);
        return ResponseEntity.ok("Quiz published successfully!");
    }

    // DELETE /api/quizzes/{quizId}
    @DeleteMapping("/{quizId}")
    public ResponseEntity<String> deleteQuiz(
            @PathVariable Long quizId) {
        assessmentService.deleteQuiz(quizId);
        return ResponseEntity.ok("Quiz deleted successfully!");
    }
}