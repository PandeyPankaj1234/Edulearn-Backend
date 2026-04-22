package com.edulearn.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private Long courseId;
    private String title;
    private String description;
    private String category;
    private String level;
    private Double price;
    private Long instructorId;
    private String thumbnailUrl;
    private String language;
    private Boolean isPublished;
    private LocalDate createdAt;
}