package com.edulearn.assessment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    private String description;

    private Integer timeLimitMinutes;

    private Integer passingScore;

    private Integer maxAttempts;

    private Boolean isPublished = false;
}