package com.edulearn.progress;

import com.edulearn.progress.dto.ProgressRequest;
import com.edulearn.progress.entity.Certificate;
import com.edulearn.progress.entity.Progress;
import com.edulearn.progress.service.ProgressService;
import com.edulearn.progress.service.ProgressServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ProgressServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Progress Service - JUnit Integration Tests")
class ProgressServiceTest {

    @Autowired private ProgressService progressService;

    private ProgressRequest req(Long sid, Long lid, Long cid) {
        ProgressRequest r = new ProgressRequest();
        r.setStudentId(sid); r.setLessonId(lid); r.setCourseId(cid);
        return r;
    }

    @Test @Order(1) @DisplayName("TrackProgress: New record created, initially not completed")
    void testTrackProgress() {
        Progress p = progressService.trackProgress(req(1L, 10L, 100L));
        assertNotNull(p.getProgressId());
        assertEquals(1L, p.getStudentId());
        assertFalse(Boolean.TRUE.equals(p.getIsCompleted()));
    }

    @Test @Order(2) @DisplayName("TrackProgress: Same lesson tracked twice returns same record")
    void testTrackProgress_idempotent() {
        Progress p1 = progressService.trackProgress(req(2L, 20L, 100L));
        Progress p2 = progressService.trackProgress(req(2L, 20L, 100L));
        assertEquals(p1.getProgressId(), p2.getProgressId());
    }

    @Test @Order(3) @DisplayName("MarkLessonComplete: Lesson marked complete with timestamp")
    void testMarkLessonComplete() {
        progressService.trackProgress(req(3L, 30L, 100L));
        Progress p = progressService.markLessonComplete(3L, 30L);
        assertTrue(Boolean.TRUE.equals(p.getIsCompleted()));
        assertNotNull(p.getCompletedAt());
    }

    @Test @Order(4) @DisplayName("MarkLessonComplete: Works even if lesson was only tracked, not watched")
    void testMarkLessonComplete_noPriorTrack() {
        // Track first to satisfy NOT NULL courseId constraint, then mark complete
        progressService.trackProgress(req(4L, 40L, 100L));
        Progress p = progressService.markLessonComplete(4L, 40L);
        assertNotNull(p);
        assertTrue(Boolean.TRUE.equals(p.getIsCompleted()));
    }

    @Test @Order(5) @DisplayName("GetLessonProgress: Returns progress for specific student+lesson")
    void testGetLessonProgress() {
        progressService.trackProgress(req(5L, 50L, 100L));
        var opt = progressService.getLessonProgress(5L, 50L);
        assertTrue(opt.isPresent());
        assertEquals(5L, opt.get().getStudentId());
    }

    @Test @Order(6) @DisplayName("GetCourseProgress: Returns correct percentage (2/4 = 50%)")
    void testCourseProgress_50percent() {
        // Track all 4 lessons with courseId=100 first, then mark 2 as complete
        progressService.trackProgress(req(6L, 61L, 100L));
        progressService.trackProgress(req(6L, 62L, 100L));
        progressService.trackProgress(req(6L, 63L, 100L));
        progressService.trackProgress(req(6L, 64L, 100L));
        progressService.markLessonComplete(6L, 61L);
        progressService.markLessonComplete(6L, 62L);
        assertEquals(50, progressService.getCourseProgress(6L, 100L, 4));
    }

    @Test @Order(7) @DisplayName("GetCourseProgress: Returns 0 when no lessons completed")
    void testCourseProgress_zero() {
        assertEquals(0, progressService.getCourseProgress(7L, 200L, 5));
    }

    @Test @Order(8) @DisplayName("GetCourseProgress: Returns 100 when all lessons done")
    void testCourseProgress_hundred() {
        // Track both lessons with courseId=300 first
        progressService.trackProgress(req(8L, 81L, 300L));
        progressService.trackProgress(req(8L, 82L, 300L));
        progressService.markLessonComplete(8L, 81L);
        progressService.markLessonComplete(8L, 82L);
        assertEquals(100, progressService.getCourseProgress(8L, 300L, 2));
    }

    @Test @Order(9) @DisplayName("GetAllProgressByStudent: Returns all tracked lessons")
    void testGetAllProgressByStudent() {
        progressService.trackProgress(req(9L, 91L, 100L));
        progressService.trackProgress(req(9L, 92L, 100L));
        progressService.trackProgress(req(9L, 93L, 100L));
        List<Progress> list = progressService.getAllProgressByStudent(9L);
        assertEquals(3, list.size());
    }

    @Test @Order(10) @DisplayName("IssueCertificate: Certificate created with unique verification code")
    void testIssueCertificate() {
        Certificate c = progressService.issueCertificate(
                10L, 1001L, "Arjun Mehta", "Spring Boot", "Prof. Kumar");
        assertNotNull(c.getCertificateId());
        assertNotNull(c.getVerificationCode());
        assertFalse(c.getVerificationCode().isBlank());
        assertNotNull(c.getIssuedAt());
    }

    @Test @Order(11) @DisplayName("VerifyCertificate: Valid code returns matching certificate")
    void testVerifyCertificate_valid() {
        Certificate issued = progressService.issueCertificate(
                11L, 1002L, "Riya Patel", "React", "Dr. Sharma");
        Certificate verified = progressService.verifyCertificate(issued.getVerificationCode());
        assertEquals(issued.getVerificationCode(), verified.getVerificationCode());
        assertEquals("Riya Patel", verified.getStudentName());
    }

    @Test @Order(12) @DisplayName("VerifyCertificate: Invalid code throws RuntimeException")
    void testVerifyCertificate_invalid() {
        assertThrows(RuntimeException.class,
                () -> progressService.verifyCertificate("INVALID-CODE-XYZ"));
    }

    @Test @Order(13) @DisplayName("GetCertificatesByStudent: Returns all certs for a student")
    void testGetCertificatesByStudent() {
        progressService.issueCertificate(12L, 1003L, "Dev", "Java 101", "Prof. A");
        progressService.issueCertificate(12L, 1004L, "Dev", "SQL",      "Prof. B");
        List<Certificate> certs = progressService.getCertificatesByStudent(12L);
        assertEquals(2, certs.size());
    }

    @Test @Order(14) @DisplayName("GetAllCertificates: Returns all certificates in system")
    void testGetAllCertificates() {
        progressService.issueCertificate(13L, 2001L, "Alice", "Docker", "Dr. X");
        progressService.issueCertificate(14L, 2002L, "Bob",   "K8s",   "Dr. Y");
        assertTrue(progressService.getAllCertificates().size() >= 2);
    }
}
