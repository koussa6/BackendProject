package com.library.com.payload.dto;


import lombok.Data;

@Data
public class PaymentDto {
    private Long orderId;
    private Double amount;
}