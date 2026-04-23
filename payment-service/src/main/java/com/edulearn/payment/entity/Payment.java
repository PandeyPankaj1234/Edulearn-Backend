package com.edulearn.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private Long studentId;

    private Long courseId;

    @Column(nullable = false)
    private Double amount;

    private String status; // Success, Failed, Refunded

    private String mode; // card, wallet, UPI

    private String transactionId;

    private LocalDateTime paidAt;

    private String currency = "INR";

    @PrePersist
    protected void onCreate() {
        this.paidAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Success";
        }
    }
}