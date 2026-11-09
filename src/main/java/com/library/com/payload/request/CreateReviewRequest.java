package com.library.com.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewRequest {
    @NotNull(message = "Book ID is mandatory")
    private Long bookId;
    @NotNull(message = "Rating is mandatory")
    @Min(value = 1,message = "Rating must be at least one")
    @Max(value = 5,message = "Rating must be at max 5")
    private Integer rating;
    @NotBlank(message = "Review text is mandatory")
    @Size(min = 10,max=2000,message = "Review text must be between 10 and 2000")
    private String reviewText;
    @Size(max=200,message = "Title must not exceed 200")
    private String title;
}
