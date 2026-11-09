package com.library.com.service.impl;

import com.library.com.modal.Payment;
import com.library.com.payload.dto.PaymentDto;
import com.library.com.payload.response.PaymentInitiateResponse;
import com.library.com.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final PaymentRepository paymentRepository;

    public PaymentInitiateResponse createCODPayment(PaymentDto request) {

        Payment payment = Payment.builder()
                .method("COD")
                .status("PENDING") // paid when delivered
                .amount(request.getAmount())
                .orderId(request.getOrderId())
                .build();

        Payment saved = paymentRepository.save(payment);

        return PaymentInitiateResponse.builder()
                .paymentId(saved.getId())
                .method(saved.getMethod())
                .status(saved.getStatus())
                .build();
    }

    public PaymentInitiateResponse markAsPaid(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("PAID");

        Payment updated = paymentRepository.save(payment);

        return PaymentInitiateResponse.builder()
                .paymentId(updated.getId())
                .method(updated.getMethod())
                .status(updated.getStatus())
                .build();
    }
}