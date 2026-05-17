package com.edulearn.payment.service;

import com.edulearn.payment.dto.PaymentRequest;
import com.edulearn.payment.dto.SubscriptionRequest;
import com.edulearn.payment.entity.Payment;
import com.edulearn.payment.entity.Subscription;
import com.edulearn.payment.messaging.EventPublisher;
import com.edulearn.payment.messaging.NotificationEvent;
import com.edulearn.payment.repository.PaymentRepository;
import com.edulearn.payment.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EventPublisher eventPublisher;

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
        Payment saved = paymentRepository.save(payment);

        // Publish PAYMENT_COMPLETED → email receipt to student
        if (request.getStudentEmail() != null) {
            NotificationEvent evt = new NotificationEvent();
            evt.setEventType("PAYMENT_COMPLETED");
            evt.setRecipientEmail(request.getStudentEmail());
            evt.setRecipientName(request.getStudentName() != null ? request.getStudentName() : "Student");
            evt.setCourseName(request.getCourseName());
            evt.setAmount(request.getAmount());
            evt.setRelatedEntityId(saved.getPaymentId());
            evt.setRelatedEntityType("PAYMENT");
            evt.setEventTime(LocalDateTime.now());
            eventPublisher.publish("notification.payment.completed", evt);
        }
        return saved;
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
        Subscription saved = subscriptionRepository.save(subscription);

        // Publish SUBSCRIPTION_CREATED → email confirmation to student
        if (request.getStudentEmail() != null) {
            NotificationEvent evt = new NotificationEvent();
            evt.setEventType("SUBSCRIPTION_CREATED");
            evt.setRecipientEmail(request.getStudentEmail());
            evt.setRecipientName(request.getStudentName() != null ? request.getStudentName() : "Student");
            evt.setAmount(request.getAmountPaid());
            evt.setRelatedEntityId(saved.getSubscriptionId());
            evt.setRelatedEntityType("SUBSCRIPTION");
            evt.setEventTime(LocalDateTime.now());
            eventPublisher.publish("notification.subscription.created", evt);
        }
        return saved;
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

    @Override
    public Subscription renewSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException(
                        "Subscription not found!"));
        // Extend end date based on plan
        if (subscription.getEndDate() != null) {
            switch (subscription.getPlan() != null
                    ? subscription.getPlan() : "Monthly") {
                case "Annual" ->
                    subscription.setEndDate(
                            subscription.getEndDate().plusYears(1));
                default ->
                    subscription.setEndDate(
                            subscription.getEndDate().plusMonths(1));
            }
        }
        subscription.setStatus("Active");
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Subscription refundSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found!"));
        if ("Refunded".equals(subscription.getStatus())) {
            throw new RuntimeException("Subscription already refunded!");
        }
        subscription.setStatus("Refunded");
        subscription.setAutoRenew(false);
        Subscription saved = subscriptionRepository.save(subscription);

        // Publish SUBSCRIPTION_REFUNDED → refund notice email to student
        // Note: studentEmail not available here without a lookup — fire-and-forget with ID
        NotificationEvent evt = new NotificationEvent();
        evt.setEventType("SUBSCRIPTION_REFUNDED");
        evt.setAmount(saved.getAmountPaid());
        evt.setRelatedEntityId(subscriptionId);
        evt.setRelatedEntityType("SUBSCRIPTION");
        evt.setEventTime(LocalDateTime.now());
        eventPublisher.publish("notification.subscription.refunded", evt);
        return saved;
    }
}
