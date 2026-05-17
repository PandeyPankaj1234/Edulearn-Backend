package com.edulearn.payment;

import com.edulearn.payment.dto.PaymentRequest;
import com.edulearn.payment.dto.SubscriptionRequest;
import com.edulearn.payment.entity.Payment;
import com.edulearn.payment.entity.Subscription;
import com.edulearn.payment.service.PaymentService;
import com.edulearn.payment.service.PaymentServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PaymentServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Payment Service - JUnit Integration Tests")
class PaymentServiceTest {

    @Autowired private PaymentService paymentService;

    private PaymentRequest pay(Long sid, Long cid, Double amt) {
        PaymentRequest r = new PaymentRequest();
        r.setStudentId(sid); r.setCourseId(cid);
        r.setAmount(amt); r.setCurrency("INR"); r.setMode("card");
        return r;
    }

    private SubscriptionRequest sub(Long sid, String plan, Double amt) {
        SubscriptionRequest r = new SubscriptionRequest();
        r.setStudentId(sid); r.setPlan(plan); r.setAmountPaid(amt); r.setAutoRenew(false);
        return r;
    }

    @Test @Order(1) @DisplayName("ProcessPayment: Stored with SUCCESS status")
    void testProcessPayment() {
        Payment p = paymentService.processPayment(pay(1L, 101L, 999.0));
        assertNotNull(p.getPaymentId());
        assertEquals("Success", p.getStatus());
        assertNotNull(p.getTransactionId());
    }

    @Test @Order(2) @DisplayName("ProcessPayment: Unique transaction IDs per payment")
    void testUniqueTransactionIds() {
        Payment p1 = paymentService.processPayment(pay(2L, 201L, 499.0));
        Payment p2 = paymentService.processPayment(pay(3L, 202L, 799.0));
        assertNotEquals(p1.getTransactionId(), p2.getTransactionId());
    }

    @Test @Order(3) @DisplayName("GetPaymentsByStudent: Returns all payments for a student")
    void testGetByStudent() {
        paymentService.processPayment(pay(4L, 301L, 199.0));
        paymentService.processPayment(pay(4L, 302L, 299.0));
        assertEquals(2, paymentService.getPaymentsByStudent(4L).size());
    }

    @Test @Order(4) @DisplayName("GetPaymentsByCourse: Returns payments for a course")
    void testGetByCourse() {
        paymentService.processPayment(pay(5L, 400L, 599.0));
        paymentService.processPayment(pay(6L, 400L, 599.0));
        paymentService.processPayment(pay(7L, 999L, 100.0));
        assertEquals(2, paymentService.getPaymentsByCourse(400L).size());
    }

    @Test @Order(5) @DisplayName("RefundPayment: Status changes to REFUNDED")
    void testRefund() {
        Payment p = paymentService.processPayment(pay(8L, 501L, 1299.0));
        assertEquals("Refunded", paymentService.refundPayment(p.getPaymentId()).getStatus());
    }

    @Test @Order(6) @DisplayName("GetTotalRevenue: Sum of successful payments")
    void testTotalRevenue() {
        paymentService.processPayment(pay(10L, 601L, 500.0));
        paymentService.processPayment(pay(11L, 602L, 300.0));
        assertTrue(paymentService.getTotalRevenue() >= 800.0);
    }

    @Test @Order(7) @DisplayName("Subscribe: Monthly plan creates Active subscription")
    void testSubscribeMonthly() {
        Subscription s = paymentService.subscribe(sub(20L, "Monthly", 299.0));
        assertEquals("Active", s.getStatus());
        assertNotNull(s.getEndDate());
    }

    @Test @Order(8) @DisplayName("Subscribe: Annual end date is start + 1 year")
    void testSubscribeAnnual() {
        Subscription s = paymentService.subscribe(sub(21L, "Annual", 1999.0));
        assertEquals(s.getStartDate().plusYears(1), s.getEndDate());
    }

    @Test @Order(9) @DisplayName("IsSubscriptionActive: False before, true after subscribing")
    void testIsActive() {
        assertFalse(paymentService.isSubscriptionActive(99L));
        paymentService.subscribe(sub(23L, "Monthly", 299.0));
        assertTrue(paymentService.isSubscriptionActive(23L));
    }

    @Test @Order(10) @DisplayName("CancelSubscription: Not active after cancellation")
    void testCancel() {
        Subscription s = paymentService.subscribe(sub(24L, "Monthly", 299.0));
        paymentService.cancelSubscription(s.getSubscriptionId());
        assertFalse(paymentService.isSubscriptionActive(24L));
    }

    @Test @Order(11) @DisplayName("GetAllActiveSubscriptions: Only Active returned")
    void testAllActive() {
        paymentService.subscribe(sub(30L, "Monthly", 299.0));
        paymentService.subscribe(sub(31L, "Annual", 1999.0));
        Subscription toCancel = paymentService.subscribe(sub(32L, "Monthly", 299.0));
        paymentService.cancelSubscription(toCancel.getSubscriptionId());
        List<Subscription> active = paymentService.getAllActiveSubscriptions();
        active.forEach(s -> assertEquals("Active", s.getStatus()));
        assertTrue(active.size() >= 2);
    }

    @Test @Order(12) @DisplayName("GetAllPayments: Returns all system payments")
    void testGetAllPayments() {
        paymentService.processPayment(pay(40L, 701L, 100.0));
        paymentService.processPayment(pay(41L, 702L, 200.0));
        assertTrue(paymentService.getAllPayments().size() >= 2);
    }
}
