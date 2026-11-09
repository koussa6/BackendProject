package com.library.com.service;

import com.library.com.payload.dto.BookReviewDTO;
import com.library.com.payload.request.CreateReviewRequest;
import com.library.com.payload.request.UpdateReviewRequest;
import com.library.com.payload.response.PageResponse;

public interface ReviewBookService {
    BookReviewDTO createReview(CreateReviewRequest request) throws Exception;

    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception;

    void deleteReview(Long reviewId) throws Exception;

    PageResponse<BookReviewDTO> getReviewsByBookId(Long id, int page, int size) throws Exception;
}
