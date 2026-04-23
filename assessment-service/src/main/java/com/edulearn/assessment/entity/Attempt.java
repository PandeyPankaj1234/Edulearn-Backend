package com.edulearn.assessment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    @Column(nullable = false)
    private Long quizId;

    @Column(nullable = false)
    private Long studentId;

    private Integer score;

    private Boolean passed;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "attempt_answers",
            joinColumns = @JoinColumn(name = "attempt_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer")
    private Map<Long, String> answers;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
    }
}