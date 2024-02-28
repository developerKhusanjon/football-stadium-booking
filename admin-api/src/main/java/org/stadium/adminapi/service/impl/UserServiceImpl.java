package org.stadium.adminapi.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.mapper.UserMapper;
import org.stadium.adminapi.service.UserService;
import org.stadium.adminapi.service.dto.UserDto;
import org.stadium.corelib.domain.User;
import org.stadium.corelib.repo.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        return userMapper.toDto(userRepository.findAll(pageable).getContent());
    }

    @Override
    public UserDto getOne(Long id) throws BadRequestAlertException {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("User not found", "user", "id", HttpStatus.NOT_FOUND));
        return userMapper.toDto(user);
    }

}
