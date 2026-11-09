package com.library.com.payload.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFineRequest {
    @NotNull(message = "Book loan is mandatory")
    private Long bookLoanId;
    @NotNull(message = "Type is mandatory")
    private String type;
    @NotNull(message = "Amount is mandatory")
    @PositiveOrZero(message = "Amount must be positive")
    private Long amount;
    private String reason;
    private String notes;
}
