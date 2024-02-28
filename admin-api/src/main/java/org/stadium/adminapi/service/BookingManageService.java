package org.stadium.adminapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.BookingDto;

import java.time.LocalDateTime;

public interface BookingManageService {
    Page<BookingDto> findAllByStadiumId(Pageable pageable, Long stadiumId);

    Page<BookingDto> findAllByTimeRange(Pageable pageable, LocalDateTime from, LocalDateTime to);

    BookingDto findById(Long id) throws BadRequestAlertException;

    AlertResponseDto cancelBookingById(Long id);

    AlertResponseDto deleteById(Long id);
}
