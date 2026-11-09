package com.library.com.modal;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method; // COD

    private String status; // PENDING, PAID

    private Double amount;

    @Column(name = "order_id")
    private Long orderId;
}