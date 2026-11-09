package com.library.com.service;

import com.library.com.exception.UserException;
import com.library.com.payload.dto.WishlistDTO;
import com.library.com.payload.response.PageResponse;

public interface WishlistService {
    WishlistDTO addToWishlist(Long bookId, String notes) throws Exception;
    void removeFromWishlist(Long bookId) throws Exception;
    PageResponse<WishlistDTO> getMyWishlist(int page, int size) throws UserException;
}
