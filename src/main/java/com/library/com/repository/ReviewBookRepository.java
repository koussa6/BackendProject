package com.library.com.repository;

import com.library.com.modal.Book;
import com.library.com.modal.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBookRepository extends JpaRepository<BookReview,Long> {
    Page<BookReview>findByBook(Book book, Pageable pageable);
    boolean existsByUserIdAndBookId(Long userId,Long bookId);
}
