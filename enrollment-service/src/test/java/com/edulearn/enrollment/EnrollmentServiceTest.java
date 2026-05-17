package com.edulearn.enrollment;

import com.edulearn.enrollment.dto.EnrollmentRequest;
import com.edulearn.enrollment.entity.Enrollment;
import com.edulearn.enrollment.service.EnrollmentService;
import com.edulearn.enrollment.service.EnrollmentServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(EnrollmentServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Enrollment Service - JUnit Integration Tests")
class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    private EnrollmentRequest req(Long studentId, Long courseId) {
        EnrollmentRequest r = new EnrollmentRequest();
        r.setStudentId(studentId);
        r.setCourseId(courseId);
        return r;
    }

    @Test @Order(1)
    @DisplayName("Enroll: Creates enrollment with Active status and 0% progress")
    void testEnroll_success() {
        Enrollment e = enrollmentService.enroll(req(1L, 101L));
        assertNotNull(e);
        assertNotNull(e.getEnrollmentId());
        assertEquals(1L, e.getStudentId());
        assertEquals(101L, e.getCourseId());
        assertEquals("Active", e.getStatus());
        assertEquals(0, e.getProgressPercent());
        assertFalse(e.getCertificateIssued());
    }

    @Test @Order(2)
    @DisplayName("Enroll: Duplicate enrollment throws RuntimeException")
    void testEnroll_duplicate_throws() {
        enrollmentService.enroll(req(2L, 102L));
        assertThrows(RuntimeException.class, () -> enrollmentService.enroll(req(2L, 102L)));
    }

    @Test @Order(3)
    @DisplayName("Enroll: Two different students can enroll in same course")
    void testEnroll_differentStudents() {
        Enrollment e1 = enrollmentService.enroll(req(10L, 200L));
        Enrollment e2 = enrollmentService.enroll(req(11L, 200L));
        assertNotEquals(e1.getEnrollmentId(), e2.getEnrollmentId());
    }

    @Test @Order(4)
    @DisplayName("IsEnrolled: Returns false before, true after enrollment")
    void testIsEnrolled() {
        assertFalse(enrollmentService.isEnrolled(3L, 103L));
        enrollmentService.enroll(req(3L, 103L));
        assertTrue(enrollmentService.isEnrolled(3L, 103L));
    }

    @Test @Order(5)
    @DisplayName("GetEnrollmentsByStudent: Returns all courses for a student")
    void testGetEnrollmentsByStudent() {
        enrollmentService.enroll(req(4L, 201L));
        enrollmentService.enroll(req(4L, 202L));
        enrollmentService.enroll(req(4L, 203L));
        List<Enrollment> list = enrollmentService.getEnrollmentsByStudent(4L);
        assertEquals(3, list.size());
        list.forEach(e -> assertEquals(4L, e.getStudentId()));
    }

    @Test @Order(6)
    @DisplayName("GetEnrollmentsByCourse: Returns all students for a course")
    void testGetEnrollmentsByCourse() {
        enrollmentService.enroll(req(20L, 300L));
        enrollmentService.enroll(req(21L, 300L));
        List<Enrollment> list = enrollmentService.getEnrollmentsByCourse(300L);
        assertEquals(2, list.size());
    }

    @Test @Order(7)
    @DisplayName("Unenroll: Enrollment status becomes Cancelled")
    void testUnenroll() {
        Enrollment e = enrollmentService.enroll(req(5L, 104L));
        enrollmentService.unenroll(e.getEnrollmentId());
        Enrollment updated = enrollmentService.getEnrollmentsByStudent(5L).get(0);
        assertEquals("Cancelled", updated.getStatus());
    }

    @Test @Order(8)
    @DisplayName("UpdateProgress: Progress percent updated, status stays Active at 50%")
    void testUpdateProgress_50percent() {
        Enrollment e = enrollmentService.enroll(req(6L, 105L));
        enrollmentService.updateProgress(e.getEnrollmentId(), 50);
        Enrollment updated = enrollmentService.getEnrollmentsByStudent(6L).get(0);
        assertEquals(50, updated.getProgressPercent());
        assertEquals("Active", updated.getStatus());
    }

    @Test @Order(9)
    @DisplayName("UpdateProgress: 100% automatically marks enrollment Completed")
    void testUpdateProgress_autoComplete() {
        Enrollment e = enrollmentService.enroll(req(7L, 106L));
        enrollmentService.updateProgress(e.getEnrollmentId(), 100);
        Enrollment updated = enrollmentService.getEnrollmentsByStudent(7L).get(0);
        assertEquals(100, updated.getProgressPercent());
        assertEquals("Completed", updated.getStatus());
    }

    @Test @Order(10)
    @DisplayName("MarkComplete: Marks enrollment Completed with 100% and completedAt set")
    void testMarkComplete() {
        Enrollment e = enrollmentService.enroll(req(8L, 107L));
        enrollmentService.markComplete(e.getEnrollmentId());
        Enrollment updated = enrollmentService.getEnrollmentsByStudent(8L).get(0);
        assertEquals("Completed", updated.getStatus());
        assertEquals(100, updated.getProgressPercent());
        assertNotNull(updated.getCompletedAt());
    }

    @Test @Order(11)
    @DisplayName("GetEnrollmentCount: Returns correct count for a course")
    void testGetEnrollmentCount() {
        enrollmentService.enroll(req(30L, 400L));
        enrollmentService.enroll(req(31L, 400L));
        enrollmentService.enroll(req(32L, 400L));
        assertEquals(3L, enrollmentService.getEnrollmentCount(400L));
    }

    @Test @Order(12)
    @DisplayName("GetAllEnrollments: Returns all enrollments in the system")
    void testGetAllEnrollments() {
        enrollmentService.enroll(req(40L, 500L));
        enrollmentService.enroll(req(41L, 501L));
        List<Enrollment> all = enrollmentService.getAllEnrollments();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }
}
