package org.stadium.adminapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.mapper.BookingMapper;
import org.stadium.adminapi.service.BookingManageService;
import org.stadium.adminapi.service.dto.BookingDto;
import org.stadium.corelib.domain.Booking;
import org.stadium.corelib.repo.BookingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
public class BookingManageServiceImpl implements BookingManageService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingManageServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public Page<BookingDto> findAllByStadiumId(Pageable pageable, Long stadiumId) {
        return bookingRepository.findAllByStadiumId(stadiumId, pageable).map(bookingMapper::toDto);
    }

    @Override
    public Page<BookingDto> findAllByTimeRange(Pageable pageable, LocalDateTime from, LocalDateTime to) {
        return bookingRepository.findAllByTimeRange(pageable, from, to).map(bookingMapper::toDto);
    }

    @Override
    public BookingDto findById(Long id) throws BadRequestAlertException {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty())
            throw new BadRequestAlertException("Booking not found", "Booking", "id");

        return bookingMapper.toDto(booking.get());
    }

    @Override
    public void cancelBookingById(Long id) {
        bookingRepository.cancelBookingById(id);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
