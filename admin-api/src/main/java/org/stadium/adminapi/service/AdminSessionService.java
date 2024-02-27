package org.stadium.adminapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.stadium.adminapi.service.dto.AdminSessionDto;
import org.stadium.corelib.domain.Admin;

import javax.servlet.http.HttpServletRequest;


public interface AdminSessionService {

    Page<AdminSessionDto> findAll(Pageable pageable, Admin admin);

    void save(HttpServletRequest request, Admin admin, String token);
}
