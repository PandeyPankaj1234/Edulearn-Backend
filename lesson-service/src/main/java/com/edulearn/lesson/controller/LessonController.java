package com.edulearn.lesson.controller;

import com.edulearn.lesson.dto.LessonRequest;
import com.edulearn.lesson.dto.ResourceRequest;
import com.edulearn.lesson.entity.Lesson;
import com.edulearn.lesson.entity.Resource;
import com.edulearn.lesson.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    // POST /api/lessons
    @PostMapping
    public ResponseEntity<Lesson> addLesson(
            @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.addLesson(request));
    }

    // GET /api/lessons/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getLessonsByCourse(courseId));
    }

    // GET /api/lessons/{lessonId}
    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(
            @PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getLessonById(lessonId));
    }

    // PUT /api/lessons/{lessonId}
    @PutMapping("/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, request));
    }

    // DELETE /api/lessons/{lessonId}
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> deleteLesson(
            @PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok("Lesson deleted successfully!");
    }

    // GET /api/lessons/course/{courseId}/preview
    @GetMapping("/course/{courseId}/preview")
    public ResponseEntity<List<Lesson>> getPreviewLessons(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getPreviewLessons(courseId));
    }

    // GET /api/lessons/course/{courseId}/count
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> countLessons(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.countLessons(courseId));
    }

    // POST /api/lessons/resources
    @PostMapping("/resources")
    public ResponseEntity<Resource> addResource(
            @Valid @RequestBody ResourceRequest request) {
        return ResponseEntity.ok(lessonService.addResource(request));
    }

    // GET /api/lessons/{lessonId}/resources
    @GetMapping("/{lessonId}/resources")
    public ResponseEntity<List<Resource>> getResources(
            @PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getResourcesByLesson(lessonId));
    }

    // DELETE /api/lessons/resources/{resourceId}
    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<String> removeResource(
            @PathVariable Long resourceId) {
        lessonService.removeResource(resourceId);
        return ResponseEntity.ok("Resource removed successfully!");
    }
}