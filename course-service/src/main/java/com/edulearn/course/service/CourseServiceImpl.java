package com.edulearn.course.service;

import com.edulearn.course.dto.CourseRequest;
import com.edulearn.course.entity.Course;
import com.edulearn.course.messaging.EventPublisher;
import com.edulearn.course.messaging.NotificationEvent;
import com.edulearn.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public Course createCourse(CourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        course.setPrice(request.getPrice());
        course.setInstructorId(request.getInstructorId());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setLanguage(request.getLanguage());
        course.setTotalDuration(request.getTotalDuration());
        course.setIsPublished(false);
        course.setApprovalStatus("Draft");
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found with id: " + courseId));
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Override
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    @Override
    public Course updateCourse(Long courseId, CourseRequest request) {
        Course course = getCourseById(courseId);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        course.setPrice(request.getPrice());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setLanguage(request.getLanguage());
        course.setTotalDuration(request.getTotalDuration());
        return courseRepository.save(course);
    }

    @Override
    public void publishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.setIsPublished(true);
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public List<Course> getFeaturedCourses() {
        return courseRepository.findByIsPublished(true);
    }

    // ── Approval Workflow ────────────────────────────────────────────────────

    @Override
    public Course submitForReview(Long courseId) {
        Course course = getCourseById(courseId);
        course.setApprovalStatus("PendingReview");
        course.setRejectionReason(null);
        Course saved = courseRepository.save(course);

        // Notify admin — course awaiting review
        // Admin email should be configured; using a fixed admin email here
        NotificationEvent evt = new NotificationEvent();
        evt.setEventType("COURSE_SUBMITTED");
        evt.setRecipientEmail("pankajpandey9351@gmail.com"); // admin email
        evt.setRecipientName("Admin");
        evt.setCourseName(saved.getTitle());
        evt.setRelatedEntityId(courseId);
        evt.setRelatedEntityType("COURSE");
        evt.setEventTime(LocalDateTime.now());
        eventPublisher.publish("notification.course.submitted", evt);
        return saved;
    }

    @Override
    public Course approveCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.setApprovalStatus("Approved");
        course.setIsPublished(true);
        course.setRejectionReason(null);
        Course saved = courseRepository.save(course);

        // Notify instructor — course approved
        // instructorEmail lookup not available here; use a placeholder system
        // The frontend should pass instructor email via a separate endpoint if needed
        NotificationEvent evt = new NotificationEvent();
        evt.setEventType("COURSE_APPROVED");
        evt.setCourseName(saved.getTitle());
        evt.setRelatedEntityId(courseId);
        evt.setRelatedEntityType("COURSE");
        evt.setEventTime(LocalDateTime.now());
        eventPublisher.publish("notification.course.approved", evt);
        return saved;
    }

    @Override
    public Course rejectCourse(Long courseId, String reason) {
        Course course = getCourseById(courseId);
        course.setApprovalStatus("Rejected");
        course.setIsPublished(false);
        course.setRejectionReason(reason);
        Course saved = courseRepository.save(course);

        // Notify instructor — course rejected
        NotificationEvent evt = new NotificationEvent();
        evt.setEventType("COURSE_REJECTED");
        evt.setCourseName(saved.getTitle());
        evt.setRejectionReason(reason);
        evt.setRelatedEntityId(courseId);
        evt.setRelatedEntityType("COURSE");
        evt.setEventTime(LocalDateTime.now());
        eventPublisher.publish("notification.course.rejected", evt);
        return saved;
    }

    @Override
    public List<Course> getCoursesByApprovalStatus(String approvalStatus) {
        return courseRepository.findByApprovalStatus(approvalStatus);
    }
}