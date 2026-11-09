package com.library.com.service.impl;

import com.library.com.domain.BookLoanStatus;
import com.library.com.domain.BookLoanType;
import com.library.com.exception.BookException;
import com.library.com.exception.UserException;
import com.library.com.mapper.BookLoanMapper;
import com.library.com.modal.Book;
import com.library.com.modal.BookLoan;
import com.library.com.modal.Subscription;
import com.library.com.modal.User;
import com.library.com.payload.dto.BookLoanDto;
import com.library.com.payload.dto.SubscriptionDto;
import com.library.com.payload.request.BookLoanSearchRequest;
import com.library.com.payload.request.CheckInRequest;
import com.library.com.payload.request.CheckoutRequest;
import com.library.com.payload.request.RenewalRequest;
import com.library.com.payload.response.PageResponse;
import com.library.com.repository.BookLoanRepository;
import com.library.com.repository.BooksRepository;
import com.library.com.service.BookLoanService;
import com.library.com.service.SubscriptionService;
import com.library.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {
    private final BookLoanRepository bookLoanRepository;
    private final BooksRepository booksRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    private final BookLoanMapper bookLoanMapper;
    @Override
    public BookLoanDto checkoutBook(CheckoutRequest checkoutRequest) throws Exception {
        User user=userService.getCurrentUser();
        return checkoutBookForUser(user.getId(), checkoutRequest);
    }

    @Override
    public BookLoanDto checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception {
        User user=userService.findById(userId);
        SubscriptionDto sub=subscriptionService.getUsersActiveSubscription(user.getId());

        Book book=booksRepository.findById(checkoutRequest.getBookId())
                .orElseThrow(()->new BookException("there is no book with the id you entered"+checkoutRequest.getBookId()));
        if(!book.getActive()){
            throw new Exception("book is not active");
        }
        if(book.getAvailableCopies()<=0){
            throw new BookException("there is no available books");
        }
        if(bookLoanRepository.hasActiveCheckout(userId,book.getId())){
            throw new BookException("book has already active checkout");
        }
        long activeCheckouts=bookLoanRepository.countActiveBookLoansByUser(userId);
        int maxBooksAllowed=sub.getMaxBooksAllowed();
        if(activeCheckouts>=maxBooksAllowed){
            throw new Exception("you have reached your maximum number of books allowed");
        }
        long overDueCount=bookLoanRepository.countOverDueBooksLoansByUser(userId);
        if(overDueCount>0){
            throw new Exception("first return old overDue book!");
        }
        BookLoan bookLoan=BookLoan.builder()
                .user(user)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKOUT_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(checkoutRequest.getCheckoutDays()))
                .renewalCount(0)
                .maxRenewal(2)
                .notes(checkoutRequest.getNotes())
                .isOverDue(false)
                .overDueDays(0)

                .build();
        book.setAvailableCopies(book.getAvailableCopies()-1);
        booksRepository.save(book);
        BookLoan bookLoan1=bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(bookLoan1);
    }

    @Override
    public BookLoanDto checkinBook(CheckInRequest checkinRequest) throws Exception {
        BookLoan bookLoan=bookLoanRepository.findById(checkinRequest.getBookLoanId())
                .orElseThrow(()->new Exception("bookloan not found "));

        if(!bookLoan.isActive()){
            throw new BookException("book loan is not active");
        }
        bookLoan.setReturnDate(LocalDate.now());

        BookLoanStatus condition=checkinRequest.getCondition();
        if(condition==null){
            condition=BookLoanStatus.RETURNED;
        }
        bookLoan.setStatus(condition);
        bookLoan.setOverDueDays(0);
        bookLoan.setIsOverDue(false);
        bookLoan.setNotes("book returned by user");
        if(condition!=BookLoanStatus.LOST){
            Book book=bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            booksRepository.save(book);
        }
        BookLoan savedBook=bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBook);
    }

    @Override
    public BookLoanDto renewCheckout(RenewalRequest renewalRequest) throws Exception {
        BookLoan bookLoan=bookLoanRepository.findById(renewalRequest.getBookLoanId())
                .orElseThrow(()->new Exception("bookloan not found "));
// 2. Check if can be renewed
        if(!bookLoan.canRenew()){
            throw new BookException("book cannot be renewed");
        }

// update due date
        bookLoan.setDueDate(bookLoan.getDueDate()
                .plusDays(renewalRequest.getExtensionDays()));

        bookLoan.setRenewalCount(bookLoan.getRenewalCount()+1);

        bookLoan.setNotes("book renewed by user");

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(savedBookLoan);

    }

    @Override
    public PageResponse<BookLoanDto> getMyBookLoans(BookLoanStatus status, int page, int size) throws UserException {
        User currentUser = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;

        if (status != null) {
            // Return only active checkouts, sorted by due date
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUser(
                    status, currentUser, pageable);
        } else {
            // Return all history (both active and returned), sorted by creation date descendin
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUserId(currentUser.getId(), pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }


    @Override
    public PageResponse<BookLoanDto> getBookLoans(BookLoanSearchRequest searchRequest) {
        // 1. Build pageable with sorting, size, etc.
        Pageable pageable = createPageable(
                searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection()
        );

        Page<BookLoan> bookLoanPage;

// 2. Apply filtering logic dynamically
        if (Boolean.TRUE.equals(searchRequest.getOverdueOnly())) {
            // Fetch overdue loans
            bookLoanPage = bookLoanRepository.findByOverdueBookLoans(LocalDate.now(), pageable);
        } else if (searchRequest.getUserId() != null) {
            // Fetch loans by specific user
            bookLoanPage = bookLoanRepository.findByUserId(searchRequest.getUserId(), pageable);
        } else if (searchRequest.getBookId() != null) {
            // Fetch loans by specific book
            bookLoanPage = bookLoanRepository.findByBookId(searchRequest.getBookId(), pageable);
        } else if (searchRequest.getStatus() != null) {
            // Fetch loans by loan status
            bookLoanPage = bookLoanRepository.findByStatus(searchRequest.getStatus(), pageable);
        } else if (searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) {
            // Fetch loans within date range
            bookLoanPage = bookLoanRepository.findBookLoansByRangeDate(
                    searchRequest.getStartDate(),
                    searchRequest.getEndDate(),
                    pageable
            );
        } else {
            // Default: return all loans
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public int updateOverdueBookLoan() {
        Pageable pageable = PageRequest.of(0, 1000); // Process
        Page<BookLoan> overduePage = bookLoanRepository
                .findByOverdueBookLoans(LocalDate.now(), pageable);

        int updateCount = 0;
        for (BookLoan bookLoan : overduePage.getContent()) {
            if (bookLoan.getStatus() == BookLoanStatus.CHECKOUT_OUT) {
                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverDue(true);

                // Calculate overdue days
             int overdueDays = calculateOverdueDays(
                        bookLoan.getDueDate(), LocalDate.now());
//                bookLoan.setOverDueDays(overdueDays);
//
//                // Calculate fine
//                BigDecimal fine = fineCalculationService.calculateOverdueFine(bookLoan);

                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }
        return updateCount;
    }
    private Pageable createPageable(int page,int size,String sortBy,String sortDirection){
        if(size>0){
            size=Math.min(size,100);
            size=Math.max(size,1);
        }
        Sort sort=sortDirection.equalsIgnoreCase("ASC")
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);
    }
    private PageResponse<BookLoanDto> convertToPageResponse(Page<BookLoan> booksLoan) {
        List<BookLoanDto> bookLoanDTOS = booksLoan.getContent()
                .stream()
                .map(bookLoanMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookLoanDTOS,
                booksLoan.getNumber(),
                booksLoan.getSize(),
                booksLoan.getTotalElements(),
                booksLoan.getTotalPages(),
                booksLoan.isLast(),
                booksLoan.isFirst(),
                booksLoan.isEmpty()
        );
    }
    public int calculateOverdueDays(LocalDate dueDate, LocalDate today) {
        if(today.isBefore(dueDate) || today.isEqual(dueDate)){
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(dueDate, today);
    }
}
