package org.stadium.adminapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.BookingManageService;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.BookingDto;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingManageController {

    private final BookingManageService bookingManageService;

    public BookingManageController(BookingManageService bookingManageService) {
        this.bookingManageService = bookingManageService;
    }

    @GetMapping()
    public ResponseEntity<Page<BookingDto>> findAllByStadiumId(Pageable pageable, @RequestParam("stadiumId") Long stadiumId) {
        return ResponseEntity.ok(bookingManageService.findAllByStadiumId(pageable, stadiumId));
    }

    @GetMapping("/in-range")
    public ResponseEntity<Page<BookingDto>> findAllByTimeRange(Pageable pageable, @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from,
                                        @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) {
        return ResponseEntity.ok(bookingManageService.findAllByTimeRange(pageable, from, to));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> findById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(bookingManageService.findById(id));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<AlertResponseDto> cancelBookingById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(bookingManageService.cancelBookingById(id));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<AlertResponseDto> deleteBookingById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(bookingManageService.deleteById(id));
    }

}
