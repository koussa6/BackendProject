package com.library.com.payload.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservationRequest {
    @NotNull(message = "Book id is mandatory")
    private Long bookId;
    private String notes;
}
