package com.edulearn.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private Long courseId;

    @NotNull(message = "Amount is required")
    private Double amount;

    private String mode; // card, wallet, UPI

    private String currency = "INR";
}