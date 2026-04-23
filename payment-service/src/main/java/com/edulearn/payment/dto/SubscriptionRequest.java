package com.edulearn.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Plan is required")
    private String plan; // Free, Monthly, Annual

    private Double amountPaid;

    private Boolean autoRenew = false;
}