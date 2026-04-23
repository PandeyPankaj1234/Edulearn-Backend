package com.edulearn.payment.controller;

import com.edulearn.payment.dto.PaymentRequest;
import com.edulearn.payment.dto.SubscriptionRequest;
import com.edulearn.payment.entity.Payment;
import com.edulearn.payment.entity.Subscription;
import com.edulearn.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // POST /api/payments/process
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    // GET /api/payments/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Payment>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                paymentService.getPaymentsByStudent(studentId));
    }

    // GET /api/payments/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Payment>> getByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                paymentService.getPaymentsByCourse(courseId));
    }

    // PUT /api/payments/{paymentId}/refund
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> refundPayment(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.refundPayment(paymentId));
    }

    // GET /api/payments/revenue
    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(paymentService.getTotalRevenue());
    }

    // POST /api/payments/subscribe
    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(paymentService.subscribe(request));
    }

    // PUT /api/payments/subscriptions/{subscriptionId}/cancel
    @PutMapping("/subscriptions/{subscriptionId}/cancel")
    public ResponseEntity<String> cancelSubscription(
            @PathVariable Long subscriptionId) {
        paymentService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription cancelled successfully!");
    }

    // GET /api/payments/subscriptions/student/{studentId}
    @GetMapping("/subscriptions/student/{studentId}")
    public ResponseEntity<Subscription> getSubscription(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                paymentService.getSubscriptionByStudent(studentId));
    }

    // GET /api/payments/subscriptions/active/{studentId}
    @GetMapping("/subscriptions/active/{studentId}")
    public ResponseEntity<Boolean> isActive(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                paymentService.isSubscriptionActive(studentId));
    }

    // GET /api/payments/subscriptions/all
    @GetMapping("/subscriptions/all")
    public ResponseEntity<List<Subscription>> getAllActive() {
        return ResponseEntity.ok(
                paymentService.getAllActiveSubscriptions());
    }
}