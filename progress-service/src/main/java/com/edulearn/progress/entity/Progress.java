package com.edulearn.progress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long lessonId;

    private Integer watchedSeconds = 0;

    private Boolean isCompleted = false;

    private LocalDateTime lastAccessedAt;

    private LocalDateTime completedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastAccessedAt = LocalDateTime.now();
    }
}