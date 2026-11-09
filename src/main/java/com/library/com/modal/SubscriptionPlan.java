package com.library.com.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="subscriptionPlan")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,unique = true)
    private String planCode;
    @Column(nullable = false,length = 100)
    private String name;
    private String description;
    @Column(nullable = false)
    private Integer durationDays;
    @Column(nullable = false)
    private Long price;

    private String currency="Dollar";
    @Column(nullable = false)
    @Positive(message = "Max books must be positive")
    private Integer maxBooksAllowed;
    @Column(nullable = false)
    @Positive(message = "Max days must be positive")
    private Integer maxDaysPerBook;
    private Integer displayOrder;
    private Boolean isActive=true;
    private Boolean isFeatured=false;
    private String badgeText;
    private String adminNotes;
    private Double priceInMajorUnits;
    private Double monthlyEquivalentPrice;
    private String badge;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
