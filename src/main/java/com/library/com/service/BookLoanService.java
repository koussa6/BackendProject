package com.library.com.service;

import com.library.com.domain.BookLoanStatus;
import com.library.com.exception.UserException;
import com.library.com.payload.dto.BookLoanDto;
import com.library.com.payload.request.BookLoanSearchRequest;
import com.library.com.payload.request.CheckInRequest;
import com.library.com.payload.request.CheckoutRequest;
import com.library.com.payload.request.RenewalRequest;
import com.library.com.payload.response.PageResponse;

public interface BookLoanService {
    BookLoanDto checkoutBook(CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDto checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDto checkinBook(CheckInRequest checkinRequest) throws Exception;

    BookLoanDto renewCheckout(RenewalRequest renewalRequest) throws Exception;

    PageResponse<BookLoanDto> getMyBookLoans(BookLoanStatus status,
                                             int page, int size) throws UserException;

    PageResponse<BookLoanDto> getBookLoans(BookLoanSearchRequest request);

    int updateOverdueBookLoan();
}
