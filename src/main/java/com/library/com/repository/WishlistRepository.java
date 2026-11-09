package com.library.com.repository;

import com.library.com.modal.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Page<Wishlist> findByUserId(Long userId, Pageable pageable);
    Wishlist findByUserIdAndBookId(Long userId,Long bookId);
    void deleteByBookIdAndUserId(Long bookId,Long userId);
    boolean existsByUserIdAndBookId(Long userId,Long bookId);
}
