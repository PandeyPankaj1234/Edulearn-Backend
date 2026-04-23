package com.edulearn.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(nullable = false)
    private Long studentId;

    private String plan; // Free, Monthly, Annual

    private LocalDate startDate;

    private LocalDate endDate;

    private String status; // Active, Cancelled, Expired

    private Double amountPaid;

    private Boolean autoRenew = false;

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDate.now();
        if (this.status == null) {
            this.status = "Active";
        }
        // Plan ke hisaab se endDate set karo
        if (this.plan != null) {
            switch (this.plan) {
                case "Monthly" -> this.endDate = LocalDate.now().plusMonths(1);
                case "Annual" -> this.endDate = LocalDate.now().plusYears(1);
                default -> this.endDate = LocalDate.now().plusYears(100);
            }
        }
    }
}