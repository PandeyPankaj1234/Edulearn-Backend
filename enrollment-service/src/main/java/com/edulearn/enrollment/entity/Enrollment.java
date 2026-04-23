package com.edulearn.enrollment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    private LocalDate enrolledAt;

    private LocalDate completedAt;

    private String status; // Active, Completed, Cancelled

    private Integer progressPercent = 0;

    private Boolean certificateIssued = false;

    @PrePersist
    protected void onCreate() {
        this.enrolledAt = LocalDate.now();
        this.status = "Active";
    }
}