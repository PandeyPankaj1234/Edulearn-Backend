package com.edulearn.lesson.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceRequest {

    @NotNull(message = "Lesson ID is required")
    private Long lessonId;

    private String name;

    private String fileUrl;

    private String fileType;

    private Long sizeKb;
}