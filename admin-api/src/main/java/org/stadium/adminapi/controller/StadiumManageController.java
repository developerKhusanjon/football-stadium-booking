package org.stadium.adminapi.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.StadiumManageService;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.StadiumDto;
import org.stadium.adminapi.service.dto.StadiumInfoDto;
import org.stadium.adminapi.service.dto.StadiumRequestDto;

import javax.validation.Valid;

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

    @PostMapping(value = "/create-stadium", consumes = {"multipart/form-data"})
    public ResponseEntity<StadiumDto> save(@ModelAttribute @Valid StadiumRequestDto requestDto) throws BadRequestAlertException {
        if (requestDto.getId() != null)
            return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(stadiumManageService.save(requestDto));
    }

    @PutMapping(value = "/{id}/update-stadium", consumes = {"multipart/form-data"})
    public ResponseEntity<StadiumDto> update(@PathVariable("id") Long id, @ModelAttribute @Valid StadiumRequestDto requestDto) throws BadRequestAlertException {
        return ResponseEntity.ok(stadiumManageService.update(id, requestDto));
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

    @DeleteMapping("/delete-stadium/{id}")
    public ResponseEntity<AlertResponseDto> deleteArticle(@PathVariable Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(stadiumManageService.deleteStadiumById(id));
    }
}
