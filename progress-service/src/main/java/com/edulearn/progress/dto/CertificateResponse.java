package com.edulearn.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {

    private Long certificateId;
    private Long studentId;
    private Long courseId;
    private String studentName;
    private String courseName;
    private String instructorName;
    private LocalDate issuedAt;
    private String verificationCode;
    private String certificateUrl;
}