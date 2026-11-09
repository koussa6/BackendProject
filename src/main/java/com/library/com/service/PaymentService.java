package com.library.com.service;

import com.library.com.payload.dto.PaymentDto;
import com.library.com.payload.request.PaymentInitiateRequest;
import com.library.com.payload.request.PaymentVerifyRequest;
import com.library.com.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) throws Exception;

    PaymentDto verifyPayment(PaymentVerifyRequest req);

    Page<PaymentDto> getAllPayments(Pageable pageable);
}
