package com.edulearn.progress.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Lesson ID is required")
    private Long lessonId;

    private Integer watchedSeconds = 0;

    private Boolean isCompleted = false;
}