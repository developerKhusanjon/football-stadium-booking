package org.stadium.adminapi.service;

import org.springframework.data.domain.Pageable;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(Pageable pageable);

    UserDto getOne(Long id) throws BadRequestAlertException;
}
