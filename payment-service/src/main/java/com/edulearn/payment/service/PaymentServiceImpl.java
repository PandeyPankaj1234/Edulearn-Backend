package com.edulearn.payment.service;

import com.edulearn.payment.dto.PaymentRequest;
import com.edulearn.payment.dto.SubscriptionRequest;
import com.edulearn.payment.entity.Payment;
import com.edulearn.payment.entity.Subscription;
import com.edulearn.payment.repository.PaymentRepository;
import com.edulearn.payment.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Payment processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setStudentId(request.getStudentId());
        payment.setCourseId(request.getCourseId());
        payment.setAmount(request.getAmount());
        payment.setMode(request.getMode());
        payment.setCurrency(request.getCurrency());
        // Unique transaction ID generate karo
        payment.setTransactionId("TXN-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());
        payment.setStatus("Success");
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Payment> getPaymentsByCourse(Long courseId) {
        return paymentRepository.findByCourseId(courseId);
    }

    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found!"));
        if (payment.getStatus().equals("Refunded")) {
            throw new RuntimeException("Payment already refunded!");
        }
        payment.setStatus("Refunded");
        return paymentRepository.save(payment);
    }

    @Override
    public Subscription subscribe(SubscriptionRequest request) {
        // Check if already active subscription exists
        subscriptionRepository
                .findByStudentIdAndStatus(request.getStudentId(), "Active")
                .ifPresent(s -> {
                    throw new RuntimeException(
                            "Active subscription already exists!");
                });

        Subscription subscription = new Subscription();
        subscription.setStudentId(request.getStudentId());
        subscription.setPlan(request.getPlan());
        subscription.setAmountPaid(request.getAmountPaid());
        subscription.setAutoRenew(request.getAutoRenew());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException(
                        "Subscription not found!"));
        subscription.setStatus("Cancelled");
        subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionByStudent(Long studentId) {
        return subscriptionRepository
                .findByStudentIdAndStatus(studentId, "Active")
                .orElseThrow(() -> new RuntimeException(
                        "No active subscription found!"));
    }

    @Override
    public boolean isSubscriptionActive(Long studentId) {
        return subscriptionRepository
                .findByStudentIdAndStatus(studentId, "Active")
                .isPresent();
    }

    @Override
    public Double getTotalRevenue() {
        Double revenue = paymentRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public List<Subscription> getAllActiveSubscriptions() {
        return subscriptionRepository.findByStatus("Active");
    }
}