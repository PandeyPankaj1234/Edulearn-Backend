package com.edulearn.payment.service;

import com.edulearn.payment.dto.PaymentRequest;
import com.edulearn.payment.dto.SubscriptionRequest;
import com.edulearn.payment.entity.Payment;
import com.edulearn.payment.entity.Subscription;

import java.util.List;

public interface PaymentService {

    Payment processPayment(PaymentRequest request);

    List<Payment> getPaymentsByStudent(Long studentId);

    List<Payment> getPaymentsByCourse(Long courseId);

    Payment refundPayment(Long paymentId);

    Subscription subscribe(SubscriptionRequest request);

    void cancelSubscription(Long subscriptionId);

    Subscription getSubscriptionByStudent(Long studentId);

    boolean isSubscriptionActive(Long studentId);

    Double getTotalRevenue();

    List<Subscription> getAllActiveSubscriptions();

    Subscription renewSubscription(Long subscriptionId);

    List<Payment> getAllPayments();

    // Admin: cancel + refund a subscription
    Subscription refundSubscription(Long subscriptionId);
}