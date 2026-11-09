package com.library.com.service.impl;

import com.library.com.domain.BookLoanStatus;
import com.library.com.domain.ReservationStatus;
import com.library.com.domain.UserRole;
import com.library.com.exception.BookException;
import com.library.com.exception.UserException;
import com.library.com.mapper.ReservationMapper;
import com.library.com.modal.Book;
import com.library.com.modal.Reservation;
import com.library.com.modal.User;
import com.library.com.payload.dto.ReservationDto;
import com.library.com.payload.request.CheckoutRequest;
import com.library.com.payload.request.ReservationRequest;
import com.library.com.payload.request.ReservationSearchRequest;
import com.library.com.payload.response.PageResponse;
import com.library.com.repository.BookLoanRepository;
import com.library.com.repository.BooksRepository;
import com.library.com.repository.ReservationRepository;
import com.library.com.service.BookLoanService;
import com.library.com.service.ReservationService;
import com.library.com.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BooksRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final BookLoanService bookLoanService;
    int MAX_RESERVATIONS=5;
    @Override
    public ReservationDto createReservation(ReservationRequest reservationRequest) throws Exception {
        User user=userService.getCurrentUser();
        return createReservationForUser(reservationRequest,user.getId());
    }

    @Override
    public ReservationDto createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception {
        boolean alreadyHasLoan=bookLoanRepository.existsByUserIdAndBookIdAndStatus(
                userId,reservationRequest.getBookId(),BookLoanStatus.CHECKOUT_OUT
        );
        if(alreadyHasLoan){
            throw new Exception("you already loan the book");
        }
        User user=userService.getCurrentUser();
        Book book=bookRepository.findById(reservationRequest.getBookId())
                .orElseThrow(()->new BookException("Book wasn't found by id"));
        if(reservationRepository.hasActiveReservation(userId,book.getId())){
            throw new Exception("you have already reservation on this book");
        }

// 4. Check if book is already available
        if(book.getAvailableCopies()>0){
            throw new Exception("book is already available");
        }

// 5. check user's active reservation limit
        long activeReservations=reservationRepository
                .countActiveReservationsByUser(userId);

        if(activeReservations>=MAX_RESERVATIONS){
            throw new Exception("you have reserved "+MAX_RESERVATIONS+" times");
        }
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservation.setNotes(reservationRequest.getNotes());

        long pendingCount = reservationRepository.countPendingReservationsByBook(
                book.getId()
        );

        reservation.setQueuePosition((int) pendingCount + 1);

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    @EventListener
    public ReservationDto cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with ID: " + reservationId));

// Verify current user owns this reservation (unless admin)
        User currentUser = userService.getCurrentUser();
        if (
                !reservation.getUser().getId().equals(currentUser.getId())
                        && currentUser.getRole() != UserRole.ROLE_ADMIN
        ) {
            throw new Exception("You can only cancel your own reservations");
        }

        if (!reservation.canBeCancelled()) {
            throw new Exception("Reservation cannot be cancelled (current status: " + reservation.getStatus() + ")");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        log.info("Reservation {} cancelled by user {}", reservationId, currentUser.getId());

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    @EventListener
    public ReservationDto fulfillReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with ID: " + reservationId));

        if (reservation.getBook().getAvailableCopies() <= 0) {
            throw new Exception("Reservation is not available for pickup (current status: " + reservation.getStatus() + ")");
        }

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setFulfilledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        log.info("Reservation {} fulfilled", reservationId);

        CheckoutRequest request = new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        request.setNotes("Assign Booked by Admin");

        bookLoanService.checkoutBookForUser(reservation.getUser().getId(), request);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public PageResponse<ReservationDto> getMyReservations(ReservationSearchRequest searchRequest) throws UserException {
      User user=userService.getCurrentUser();
      searchRequest.setUserId(user.getId());
      return searchReservations(searchRequest);
    }

    @Override
    public PageResponse<ReservationDto> searchReservations(ReservationSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest);

        Page<Reservation> reservationPage = reservationRepository.searchReservationsWithFilters(
                searchRequest.getUserId(),
                searchRequest.getBookId(),
                searchRequest.getStatus(),
                searchRequest.getActiveOnly() != null ? searchRequest.getActiveOnly() : false,
                pageable
        );

        return buildPageResponse(reservationPage);
    }
    private Pageable createPageable(ReservationSearchRequest searchRequest){
        Sort sort=searchRequest.getSortDirection().equalsIgnoreCase("ASC")
                ?Sort.by(searchRequest.getSortBy()).ascending():Sort.by(searchRequest.getSortBy()).descending();
        return PageRequest.of(searchRequest.getPage(),searchRequest.getSize(),sort);
    }
    private PageResponse<ReservationDto> buildPageResponse(Page<Reservation> reservationPage) {
        List<ReservationDto> dtos = reservationPage.getContent().stream()
                .map(reservationMapper::toDTO)
                .toList();

        PageResponse<ReservationDto> response = new PageResponse<>();
        response.setContent(dtos);
        response.setPageNumber(reservationPage.getNumber());
        response.setPageSize(reservationPage.getSize());
        response.setTotalElements(reservationPage.getTotalElements());
        response.setTotalPages(reservationPage.getTotalPages());
        response.setLast(reservationPage.isLast());

        return response;
    }
}
