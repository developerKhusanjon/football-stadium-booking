package org.stadium.userapi.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.service.dto.StadiumDto;
import org.stadium.userapi.service.dto.StadiumInfoDto;

import java.time.LocalDateTime;


public interface StadiumManageService {

    Page<StadiumInfoDto> findAll(Pageable pageable);

    Page<StadiumInfoDto> findNearestAvailableByTimeRange(Pageable pageable, Double lon, Double lat, LocalDateTime from, LocalDateTime to);

    StadiumInfoDto findInfoById(Long id) throws BadRequestAlertException;

    StadiumDto findById(Long id) throws BadRequestAlertException;

    Resource downloadImage(Long imageId) throws BadRequestAlertException;

    Resource downloadCompressImage(Long imageId) throws BadRequestAlertException;

}
