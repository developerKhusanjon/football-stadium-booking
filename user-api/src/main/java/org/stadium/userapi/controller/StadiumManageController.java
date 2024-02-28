package org.stadium.userapi.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.service.StadiumManageService;
import org.stadium.userapi.service.dto.StadiumInfoDto;
import org.stadium.userapi.service.dto.StadiumDto;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/stadiums")
public class StadiumManageController {

    private final StadiumManageService stadiumManageService;

    public StadiumManageController(StadiumManageService stadiumManageService) {
        this.stadiumManageService = stadiumManageService;
    }

    @GetMapping
    public ResponseEntity<Page<StadiumInfoDto>> findAll(Pageable pageable) throws BadRequestAlertException {
        return ResponseEntity.ok(stadiumManageService.findAll(pageable));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<StadiumInfoDto> findInfoById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(stadiumManageService.findInfoById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDto> findById(@PathVariable("id") Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(stadiumManageService.findById(id));
    }

    @GetMapping(value = "/nearest")
    public ResponseEntity<Page<StadiumInfoDto>> findAllAvailableNeatest(Pageable pageable, @RequestParam("lon") Double lon, @RequestParam("lat") Double lat,
                                                                        @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from,
                                                                        @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws BadRequestAlertException {

        return ResponseEntity.ok(stadiumManageService.findNearestAvailableByTimeRange(pageable, lon, lat, from, to));
    }

    @GetMapping(value = "/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws BadRequestAlertException {
        Resource download = stadiumManageService.downloadImage(imageId);
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).header("Content-Disposition", "inline;").body(download);
    }
    @GetMapping(value = "/image/compress/download/{imageId}")
    public ResponseEntity<Resource> downloadIcon(@PathVariable Long imageId) throws BadRequestAlertException {
        Resource download = stadiumManageService.downloadCompressImage(imageId);
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).header("Content-Disposition", "inline;").body(download);
    }
}
