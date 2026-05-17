package com.edulearn.progress.service;

import com.edulearn.progress.dto.CertificateResponse;
import com.edulearn.progress.dto.ProgressRequest;
import com.edulearn.progress.entity.Certificate;
import com.edulearn.progress.entity.Progress;
import com.edulearn.progress.repository.CertificateRepository;
import com.edulearn.progress.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Progress trackProgress(ProgressRequest request) {
        // Pehle se exist karta hai toh update karo
        Optional<Progress> existing = progressRepository
                .findByStudentIdAndLessonId(
                        request.getStudentId(),
                        request.getLessonId());

        Progress progress;
        if (existing.isPresent()) {
            progress = existing.get();
            progress.setWatchedSeconds(request.getWatchedSeconds());
            if (request.getIsCompleted()) {
                progress.setIsCompleted(true);
                progress.setCompletedAt(LocalDateTime.now());
            }
        } else {
            progress = new Progress();
            progress.setStudentId(request.getStudentId());
            progress.setCourseId(request.getCourseId());
            progress.setLessonId(request.getLessonId());
            progress.setWatchedSeconds(request.getWatchedSeconds());
            progress.setIsCompleted(request.getIsCompleted());
            if (request.getIsCompleted()) {
                progress.setCompletedAt(LocalDateTime.now());
            }
        }
        return progressRepository.save(progress);
    }

    @Override
    public Progress markLessonComplete(Long studentId, Long lessonId) {
        Optional<Progress> existing = progressRepository
                .findByStudentIdAndLessonId(studentId, lessonId);

        Progress progress;
        if (existing.isPresent()) {
            progress = existing.get();
        } else {
            progress = new Progress();
            progress.setStudentId(studentId);
            progress.setLessonId(lessonId);
        }
        progress.setIsCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        return progressRepository.save(progress);
    }

    @Override
    public int getCourseProgress(Long studentId, Long courseId,
                                 int totalLessons) {
        if (totalLessons == 0) return 0;
        long completed = progressRepository
                .countCompletedByStudentIdAndCourseId(studentId, courseId);
        return (int) ((completed * 100) / totalLessons);
    }

    @Override
    public Optional<Progress> getLessonProgress(Long studentId,
                                                Long lessonId) {
        return progressRepository
                .findByStudentIdAndLessonId(studentId, lessonId);
    }

    @Override
    public Certificate issueCertificate(Long studentId, Long courseId,
                                        String studentName,
                                        String courseName,
                                        String instructorName) {
        // Already issued check
        if (certificateRepository.existsByStudentIdAndCourseId(
                studentId, courseId)) {
            return certificateRepository
                    .findByStudentIdAndCourseId(studentId, courseId)
                    .orElseThrow();
        }

        Certificate certificate = new Certificate();
        certificate.setStudentId(studentId);
        certificate.setCourseId(courseId);
        certificate.setStudentName(studentName);
        certificate.setCourseName(courseName);
        certificate.setInstructorName(instructorName);
        certificate.setCertificateUrl(
                "/certificates/" + studentId + "/" + courseId);
        return certificateRepository.save(certificate);
    }

    @Override
    public CertificateResponse getCertificate(Long studentId, Long courseId) {
        Certificate cert = certificateRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException(
                        "Certificate not found!"));

        return new CertificateResponse(
                cert.getCertificateId(),
                cert.getStudentId(),
                cert.getCourseId(),
                cert.getStudentName(),
                cert.getCourseName(),
                cert.getInstructorName(),
                cert.getIssuedAt(),
                cert.getVerificationCode(),
                cert.getCertificateUrl()
        );
    }

    @Override
    public Certificate verifyCertificate(String verificationCode) {
        return certificateRepository
                .findByVerificationCode(verificationCode)
                .orElseThrow(() -> new RuntimeException(
                        "Invalid verification code!"));
    }

    @Override
    public List<Progress> getAllProgressByStudent(Long studentId) {
        return progressRepository.findByStudentId(studentId);
    }

    @Override
    public List<Certificate> getCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
}