package com.edulearn.course.service;

import com.edulearn.course.dto.CourseRequest;
import com.edulearn.course.entity.Course;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseRequest request);

    List<Course> getAllCourses();

    Course getCourseById(Long courseId);

    List<Course> getCoursesByCategory(String category);

    List<Course> getCoursesByInstructor(Long instructorId);

    List<Course> searchCourses(String keyword);

    Course updateCourse(Long courseId, CourseRequest request);

    void publishCourse(Long courseId);

    void deleteCourse(Long courseId);

    List<Course> getFeaturedCourses();

    // ── Approval Workflow ───────────────────────────────────────────────────
    Course submitForReview(Long courseId);

    Course approveCourse(Long courseId);

    Course rejectCourse(Long courseId, String reason);

    List<Course> getCoursesByApprovalStatus(String approvalStatus);
}