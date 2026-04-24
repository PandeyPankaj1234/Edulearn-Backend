package com.edulearn.discussion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThreadRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long lessonId;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Body is required")
    private String body;
}