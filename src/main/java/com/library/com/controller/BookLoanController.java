package com.library.com.controller;


import com.library.com.domain.BookLoanStatus;
import com.library.com.payload.dto.BookLoanDto;
import com.library.com.payload.request.BookLoanSearchRequest;
import com.library.com.payload.request.CheckInRequest;
import com.library.com.payload.request.CheckoutRequest;
import com.library.com.payload.request.RenewalRequest;
import com.library.com.payload.response.ApiResponse;
import com.library.com.payload.response.PageResponse;
import com.library.com.service.BookLoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/book-loans")
@RestController
@Tag(name = "Book loan controller")

public class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping
    public ResponseEntity<?> checkoutBook(
            @Valid @RequestBody CheckoutRequest checkoutRequest
            ) throws Exception {
            BookLoanDto dto = bookLoanService.checkoutBook(checkoutRequest);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PostMapping("/checkout/user/{userId}")
    @PostAuthorize("returnObject.user.name==authentication.name")
    public ResponseEntity<?> checkoutBookForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {

        BookLoanDto bookLoan = bookLoanService
                .checkoutBookForUser(userId, checkoutRequest);
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);

    }
    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(
            @Valid @RequestBody CheckInRequest checkinRequest) throws Exception {
        BookLoanDto bookLoan = bookLoanService
                .checkinBook(checkinRequest);
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
    }
    @PostMapping("/renew")
    public ResponseEntity<?> renew(
            @Valid @RequestBody RenewalRequest renewalRequest) throws Exception {

        BookLoanDto bookLoan = bookLoanService
                .renewCheckout(renewalRequest);
        return new ResponseEntity<>(bookLoan, HttpStatus.OK);

    }
    @GetMapping("/my")
    public ResponseEntity<?> getMyBookLoans(
            @RequestParam(required = false) BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws Exception {

        PageResponse<BookLoanDto> bookLoans = bookLoanService
                .getMyBookLoans(status, page, size);
        return ResponseEntity.ok(bookLoans);
    }
    @PostMapping("/search")
    public ResponseEntity<?> getAllBookLoans(
            @RequestBody BookLoanSearchRequest searchRequest) throws Exception {

        PageResponse<BookLoanDto> bookLoans = bookLoanService
                .getBookLoans(searchRequest);
        return ResponseEntity.ok(bookLoans);

    }
    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBookLoans() {

        int updateCount = bookLoanService.updateOverdueBookLoan();
        return ResponseEntity.ok(
                new ApiResponse("overdue book loans are updated", true));

    }
}
