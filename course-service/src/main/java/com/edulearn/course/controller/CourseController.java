package com.edulearn.course.controller;

import com.edulearn.course.dto.CourseRequest;
import com.edulearn.course.entity.Course;
import com.edulearn.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")

public class CourseController {

    @Autowired
    private CourseService courseService;

    // POST /api/courses
    @PostMapping
    public ResponseEntity<Course> createCourse(
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    // GET /api/courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // GET /api/courses/{courseId}
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    // GET /api/courses/category/{category}
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Course>> getCoursesByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }

    // GET /api/courses/instructor/{instructorId}
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    // GET /api/courses/search?keyword=java
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    // GET /api/courses/featured
    @GetMapping("/featured")
    public ResponseEntity<List<Course>> getFeaturedCourses() {
        return ResponseEntity.ok(courseService.getFeaturedCourses());
    }

    // PUT /api/courses/{courseId}
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, request));
    }

    // PUT /api/courses/{courseId}/publish
    @PutMapping("/{courseId}/publish")
    public ResponseEntity<String> publishCourse(
            @PathVariable Long courseId) {
        courseService.publishCourse(courseId);
        return ResponseEntity.ok("Course published successfully!");
    }

    // DELETE /api/courses/{courseId}
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted successfully!");
    }

    // ── Approval Workflow ────────────────────────────────────────────────────

    // PUT /api/courses/{courseId}/submit-review  — Instructor submits for admin review
    @PutMapping("/{courseId}/submit-review")
    public ResponseEntity<Course> submitForReview(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.submitForReview(courseId));
    }

    // PUT /api/courses/{courseId}/approve  — Admin approves + publishes
    @PutMapping("/{courseId}/approve")
    public ResponseEntity<Course> approveCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.approveCourse(courseId));
    }

    // PUT /api/courses/{courseId}/reject  — Admin rejects with reason
    @PutMapping("/{courseId}/reject")
    public ResponseEntity<Course> rejectCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "Does not meet quality standards")
            String reason) {
        return ResponseEntity.ok(courseService.rejectCourse(courseId, reason));
    }

    // GET /api/courses/admin/approval-status?status=PendingReview
    @GetMapping("/admin/approval-status")
    public ResponseEntity<List<Course>> getCoursesByApprovalStatus(
            @RequestParam String status) {
        return ResponseEntity.ok(courseService.getCoursesByApprovalStatus(status));
    }
}