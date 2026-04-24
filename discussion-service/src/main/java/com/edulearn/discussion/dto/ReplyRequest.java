package com.edulearn.discussion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyRequest {

    @NotNull(message = "Thread ID is required")
    private Long threadId;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotBlank(message = "Body is required")
    private String body;
}