package org.stadium.adminapi.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.dto.AdminDto;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.JWTTokenDto;

public interface AdminService {

    Page<AdminDto> findAll(Pageable pageable);

    AdminDto findOne(Long id) throws BadRequestAlertException;

    JWTTokenDto save(AdminDto userDto) throws Exception;

    AlertResponseDto delete(Long id) throws Exception;
}
