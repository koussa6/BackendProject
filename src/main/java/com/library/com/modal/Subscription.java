package com.library.com.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private SubscriptionPlan plan;
    private String planName;
    private String planCode;
    private Long price;

    @Column(nullable = false)
    private Integer maxBooksAllowed;
    @Column(nullable = false)
    private Integer maxDaysPerBook;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private Boolean isActive=true;
    private Boolean autoRenew;

    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Boolean isValid(){
        if(!isActive){return false;}
        LocalDate time=LocalDate.now();
        return !time.isBefore(startDate) && !time.isAfter(endDate);
}
    public Boolean isExpired(){
        return LocalDate.now().isAfter(endDate);
}
    public long getDaysRemaining(){
        if(isExpired()){
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(),endDate);
}
    public void calculateEndDate() {
        if (plan != null && startDate != null) {
            this.endDate = startDate.plusDays(plan.getDurationDays());
        }
    }

    public void initializeFromPlan() {
        if (plan == null) {
            this.planName = plan.getName();
            this.planCode = plan.getPlanCode();
            this.price = plan.getPrice();
            this.maxBooksAllowed = plan.getMaxBooksAllowed();
            this.maxDaysPerBook = plan.getMaxDaysPerBook();
            if (startDate == null) {
                this.startDate = LocalDate.now();
            }
            calculateEndDate();
        }
    }
}
