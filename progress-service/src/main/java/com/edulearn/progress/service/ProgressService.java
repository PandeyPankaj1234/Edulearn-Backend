package com.edulearn.progress.service;

import com.edulearn.progress.dto.CertificateResponse;
import com.edulearn.progress.dto.ProgressRequest;
import com.edulearn.progress.entity.Certificate;
import com.edulearn.progress.entity.Progress;
import java.util.Optional;

import java.util.List;

public interface ProgressService {

    Progress trackProgress(ProgressRequest request);

    Progress markLessonComplete(Long studentId, Long lessonId);

    int getCourseProgress(Long studentId, Long courseId, int totalLessons);

    Optional<Progress> getLessonProgress(Long studentId, Long lessonId);

    Certificate issueCertificate(Long studentId, Long courseId,
                                 String studentName, String courseName,
                                 String instructorName);

    CertificateResponse getCertificate(Long studentId, Long courseId);

    Certificate verifyCertificate(String verificationCode);

    List<Progress> getAllProgressByStudent(Long studentId);

    List<Certificate> getCertificatesByStudent(Long studentId);
}