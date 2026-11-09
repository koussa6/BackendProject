package com.library.com.modal;


import com.library.com.domain.BookLoanStatus;
import com.library.com.domain.BookLoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookloan")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Book book;
    private BookLoanType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookLoanStatus status;
    @Column(nullable = false)
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Column(nullable = false)
    private Integer renewalCount=0;
    @Column(nullable = false)
    private Integer maxRenewal=2;
    @Column(length = 200)
    private String notes;
    @Column(nullable = false)
    private Boolean isOverDue=false;
    @Column(nullable = false)
    private Integer overDueDays=0;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Boolean isActive(){
        return status==BookLoanStatus.CHECKOUT_OUT||status==BookLoanStatus.OVERDUE;
    }
    public Boolean canRenew(){
        return status==BookLoanStatus.CHECKOUT_OUT
                && !isOverDue && renewalCount<maxRenewal;
    }
}
