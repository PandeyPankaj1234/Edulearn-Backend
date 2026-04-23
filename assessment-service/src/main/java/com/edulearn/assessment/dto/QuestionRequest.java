package com.edulearn.assessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @NotBlank(message = "Question text is required")
    private String text;

    private String type; // MCQ, TrueFalse

    private List<String> options;

    private String correctAnswer;

    private Integer marks;

    private Integer orderIndex;
}