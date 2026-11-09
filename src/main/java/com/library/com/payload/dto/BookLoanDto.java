package com.library.com.payload.dto;

import com.library.com.domain.BookLoanStatus;
import com.library.com.domain.BookLoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookLoanDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private String bookAuthor;
    private String bookCoverImage;
    private BookLoanType type;
    private BookLoanStatus status;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private Long remainingDays;
    private LocalDate returnDate;
    private Integer renewalCount;
    private Integer maxRenewals;
    private Integer overDueDays;
    private BigDecimal fineAmount;
    private Boolean finePaid;
    private String notes;
    private Boolean isOverdue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
