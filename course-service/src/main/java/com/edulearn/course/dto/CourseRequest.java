package com.edulearn.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private String level;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    private String thumbnailUrl;

    private String language;

    private Integer totalDuration;
}