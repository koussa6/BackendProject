package com.library.com.controller;


import com.library.com.payload.dto.BookReviewDTO;
import com.library.com.payload.request.CreateReviewRequest;
import com.library.com.payload.request.UpdateReviewRequest;
import com.library.com.payload.response.ApiResponse;
import com.library.com.payload.response.PageResponse;
import com.library.com.service.ReviewBookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Book review controller")

public class BookReviewController {

    private final ReviewBookService bookReviewService;

    @PostMapping
    public ResponseEntity<?> createReview(
            @Valid @RequestBody CreateReviewRequest request
    ) throws Exception {
        BookReviewDTO reviewDTO = bookReviewService.createReview(request);
        return ResponseEntity.ok(reviewDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request
    ) throws Exception {
        BookReviewDTO reviewDTO = bookReviewService.updateReview(id,request);
        return ResponseEntity.ok(reviewDTO);
    }
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) throws Exception {
        bookReviewService.deleteReview(reviewId);
        return ResponseEntity.ok(
                new ApiResponse("Review deleted successfully", true));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<BookReviewDTO>> getReviewsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {

        PageResponse<BookReviewDTO> reviews = bookReviewService
                .getReviewsByBookId(bookId, page, size);

        return ResponseEntity.ok(reviews);
    }
}
