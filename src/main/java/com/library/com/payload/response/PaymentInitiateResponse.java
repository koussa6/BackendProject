package com.library.com.payload.response;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInitiateResponse {
    private Long paymentId;
    private String method;
    private String status;
}