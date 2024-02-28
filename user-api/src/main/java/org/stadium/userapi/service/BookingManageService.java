package org.stadium.userapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.corelib.domain.User;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.service.dto.AlertResponseDto;
import org.stadium.userapi.service.dto.BookingDto;
import org.stadium.userapi.service.dto.UserDto;

import java.time.LocalDateTime;

public interface BookingManageService {
    Page<BookingDto> findAllByUserId(Pageable pageable, Long userId);

    BookingDto findById(Long id) throws BadRequestAlertException;

    BookingDto bookStadiumByUserAndId(User user, Long stadiumId, LocalDateTime from, LocalDateTime to)  throws BadRequestAlertException;

    AlertResponseDto cancelBookingByUserAndId(Long bookingId, User user) throws BadRequestAlertException;
}
