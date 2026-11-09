package com.library.com.controller;

import com.library.com.payload.dto.PaymentDto;
import com.library.com.payload.response.PaymentInitiateResponse;
import com.library.com.service.impl.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment controller")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    // Create COD payment
    @PostMapping("/cod")
    public ResponseEntity<PaymentInitiateResponse> createCOD(
            @RequestBody PaymentDto request) {

        return ResponseEntity.ok(
                paymentService.createCODPayment(request)
        );
    }

    // Mark as paid (after delivery)
    @PutMapping("/cod/{orderId}/paid")
    public ResponseEntity<PaymentInitiateResponse> markAsPaid(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.markAsPaid(orderId)
        );
    }
}