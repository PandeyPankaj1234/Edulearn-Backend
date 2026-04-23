package com.edulearn.progress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    private LocalDate issuedAt;

    private String certificateUrl;

    @Column(unique = true)
    private String verificationCode;

    private String instructorName;

    private String courseName;

    private String studentName;

    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDate.now();
        this.verificationCode = UUID.randomUUID().toString();
    }
}