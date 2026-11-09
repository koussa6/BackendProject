package com.library.com.repository;

import com.library.com.domain.BookLoanStatus;
import com.library.com.modal.BookLoan;
import com.library.com.modal.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    Page<BookLoan> findByUserId(Long userId, Pageable pageable);

    Page<BookLoan> findByStatusAndUser(BookLoanStatus status, User user, Pageable pageable);

    Page<BookLoan> findByStatus(BookLoanStatus status, Pageable pageable);

    List<BookLoan> findByBookId(Long bookId);

    Page<BookLoan> findByBookId(Long bookId, Pageable pageable);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BookLoanStatus status);

    @Query("""
        select case when count(bl) > 0 then true else false end
        from BookLoan bl
        where bl.user.id = :userId
          and bl.book.id = :bookId
          and bl.status in (com.library.com.domain.BookLoanStatus.CHECKOUT_OUT,
                            com.library.com.domain.BookLoanStatus.OVERDUE)
        """)
    boolean hasActiveCheckout(@Param("userId") Long userId,
                              @Param("bookId") Long bookId);

    @Query("""
        select count(bl) from BookLoan bl
        where bl.user.id = :userId
          and bl.status in (com.library.com.domain.BookLoanStatus.CHECKOUT_OUT,
                            com.library.com.domain.BookLoanStatus.OVERDUE)
        """)
    long countActiveBookLoansByUser(@Param("userId") Long userId);

    @Query("""
        select count(bl) from BookLoan bl
        where bl.user.id = :userId
          and bl.status = com.library.com.domain.BookLoanStatus.OVERDUE
        """)
    long countOverDueBooksLoansByUser(@Param("userId") Long userId);

    @Query("""
        select bl from BookLoan bl
        where bl.dueDate < :currentDate
          and bl.status in (com.library.com.domain.BookLoanStatus.CHECKOUT_OUT,
                            com.library.com.domain.BookLoanStatus.OVERDUE)
        """)
    Page<BookLoan> findByOverdueBookLoans(@Param("currentDate") LocalDate currentDate,
                                          Pageable pageable);

    @Query("select bl from BookLoan bl where bl.checkoutDate between :startDate and :endDate")
    Page<BookLoan> findBookLoansByRangeDate(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            Pageable pageable);
}