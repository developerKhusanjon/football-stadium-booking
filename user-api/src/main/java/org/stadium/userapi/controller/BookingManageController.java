package org.stadium.userapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stadium.corelib.domain.User;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.security.CurrentUser;
import org.stadium.userapi.service.BookingManageService;
import org.stadium.userapi.service.dto.AlertResponseDto;
import org.stadium.userapi.service.dto.BookingDto;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingManageController {

    private final BookingManageService bookingManageService;

    public BookingManageController(BookingManageService bookingManageService) {
        this.bookingManageService = bookingManageService;
    }

    @GetMapping()
    public ResponseEntity<Page<BookingDto>> findAllByUserId(Pageable pageable, @RequestParam("userID") Long userID) {
        return ResponseEntity.ok(bookingManageService.findAllByUserId(pageable, userID));
    }

//    @GetMapping("/in-range")
//    public ResponseEntity<Page<BookingDto>> findAllByTimeRange(Pageable pageable, @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//                                        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
//        return ResponseEntity.ok(bookingManageService.findByRange(pageable, from, to));
//    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> findById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(bookingManageService.findById(id));
    }

    @PostMapping("/book")
    public ResponseEntity<BookingDto> bookStadiumByUserAndId( @RequestParam("stadiumID") Long stadiumId, @CurrentUser User user,
                                                                        @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from,
                                                                        @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws BadRequestAlertException {

        return ResponseEntity.ok(bookingManageService.bookStadiumByUserAndId(user, stadiumId, from, to));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<AlertResponseDto> cancelBookingByUserAndId(@PathVariable("id") Long id,  @CurrentUser User user) throws BadRequestAlertException {
        return ResponseEntity.ok(bookingManageService.cancelBookingByUserAndId(id, user));
    }

}
