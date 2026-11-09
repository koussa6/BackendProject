package com.library.com.controller;

import com.library.com.domain.ReservationStatus;
import com.library.com.exception.BookException;
import com.library.com.exception.UserException;
import com.library.com.payload.dto.ReservationDto;
import com.library.com.payload.request.ReservationRequest;
import com.library.com.payload.request.ReservationSearchRequest;
import com.library.com.payload.response.PageResponse;
import com.library.com.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@Tag(name = "Reservation controller")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping()
    public ResponseEntity<?> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest
    ) throws Exception {
        ReservationDto reservationDTO = reservationService
                .createReservation(reservationRequest);
        return ResponseEntity.ok(reservationDTO);
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createReservationForUser(
            @PathVariable Long userId,
            @Valid @RequestBody ReservationRequest reservationRequest) throws Exception {

        ReservationDto reservation = reservationService
                .createReservationForUser(reservationRequest, userId);

        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long id) throws Exception {

        ReservationDto reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillReservation(@PathVariable Long id)
            throws BookException, Exception {

        ReservationDto reservation = reservationService.fulfillReservation(id);
        return ResponseEntity.ok(reservation);
    }
    @GetMapping("/my")
    public ResponseEntity<PageResponse<ReservationDto>> getMyReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) throws UserException {

        ReservationSearchRequest searchRequest = new ReservationSearchRequest();
        searchRequest.setStatus(status);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<ReservationDto> reservations = reservationService.getMyReservations(searchRequest);
        return ResponseEntity.ok(reservations);
    }
}