package com.edulearn.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "Title is required")
    private String title;

    private String contentType;

    private String contentUrl;

    private Integer durationMinutes;

    private Integer orderIndex;

    private String description;

    private Boolean isPreview = false;
}