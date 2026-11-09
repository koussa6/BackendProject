package com.library.com.service;

import com.library.com.exception.UserException;
import com.library.com.payload.dto.ReservationDto;
import com.library.com.payload.request.ReservationRequest;
import com.library.com.payload.request.ReservationSearchRequest;
import com.library.com.payload.response.PageResponse;

public interface ReservationService {
    ReservationDto createReservation(ReservationRequest reservationRequest) throws Exception;

    ReservationDto createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception;

    ReservationDto cancelReservation(Long reservationId) throws Exception;

    ReservationDto fulfillReservation(Long reservationId) throws Exception;
    PageResponse<ReservationDto> getMyReservations(ReservationSearchRequest searchRequest) throws UserException;

    /**
     * Get my reservations (current user) with filters
     * @param searchRequest Search criteria
     * @return Paginated reservations
     */
    PageResponse<ReservationDto> searchReservations(ReservationSearchRequest searchRequest);
}
