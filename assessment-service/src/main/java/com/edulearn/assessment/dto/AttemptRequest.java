package com.edulearn.assessment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class AttemptRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private Map<Long, String> answers;
}