package org.stadium.userapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stadium.corelib.domain.Booking;
import org.stadium.corelib.domain.Stadium;
import org.stadium.corelib.domain.User;
import org.stadium.corelib.repo.BookingRepository;
import org.stadium.corelib.repo.StadiumInfoRepository;
import org.stadium.corelib.repo.StadiumRepository;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.mapper.BookingMapper;
import org.stadium.userapi.service.BookingManageService;
import org.stadium.userapi.service.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingManageServiceImpl implements BookingManageService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final StadiumInfoRepository stadiumInfoRepository;

    private final StadiumRepository stadiumRepository;

    public BookingManageServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, StadiumInfoRepository stadiumInfoRepository, StadiumRepository stadiumRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.stadiumInfoRepository = stadiumInfoRepository;
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public Page<BookingDto> findAllByUserId(Pageable pageable, Long userId) {
        return null;
    }

    @Override
    public BookingDto findById(Long id) throws BadRequestAlertException {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty())
            throw new BadRequestAlertException("Booking not found", "Booking", "id");

        return bookingMapper.toDto(booking.get());
    }

    @Override
    public BookingDto bookStadiumByUserAndId(User user, Long stadiumId, LocalDateTime from, LocalDateTime to) throws BadRequestAlertException {
        Optional<Stadium> stadium = stadiumRepository.findById(stadiumId);
        if (stadium.isEmpty())
            throw new BadRequestAlertException("Stadium not found", "Stadium", "id");

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setStadium(stadium.get());
        booking.setFromHour(from);
        booking.setTillHour(to);
        booking.setBooked(true);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public void cancelBookingByUserAndId(Long bookingId, User user) throws BadRequestAlertException {
        boolean check = bookingRepository.checkForBooked(bookingId, user.getId());
        if (!check)
            throw new BadRequestAlertException("Stadium not booked by this user", "Booking", "id");

        bookingRepository.cancelBookingById(bookingId);
    }
}
