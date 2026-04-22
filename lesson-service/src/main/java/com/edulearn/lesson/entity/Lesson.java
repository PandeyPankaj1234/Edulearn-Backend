package com.edulearn.lesson.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    private String contentType; // video, article, pdf

    private String contentUrl;

    private Integer durationMinutes;

    private Integer orderIndex;

    private String description;

    private Boolean isPreview = false;
}