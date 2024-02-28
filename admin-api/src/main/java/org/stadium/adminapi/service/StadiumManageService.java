package org.stadium.adminapi.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.StadiumDto;
import org.stadium.adminapi.service.dto.StadiumInfoDto;
import org.stadium.adminapi.service.dto.StadiumRequestDto;


public interface StadiumManageService {

    Page<StadiumInfoDto> findAll(Pageable pageable) throws BadRequestAlertException;

    StadiumInfoDto findInfoById(Long id) throws BadRequestAlertException;

    StadiumDto findById(Long id) throws BadRequestAlertException;

    StadiumDto save(StadiumRequestDto requestDto) throws BadRequestAlertException;

    StadiumDto update(Long id, StadiumRequestDto requestDto) throws BadRequestAlertException;

    Resource downloadImage(Long imageId) throws BadRequestAlertException;

    Resource downloadCompressImage(Long imageId) throws BadRequestAlertException;

    AlertResponseDto deleteStadiumById(Long id);

    void deleteImageById(Long id);
}
