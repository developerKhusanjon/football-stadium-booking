package org.stadium.adminapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.controller.vm.LoginVM;
import org.stadium.adminapi.mapper.AdminMapper;
import org.stadium.adminapi.service.AdminService;
import org.stadium.adminapi.service.dto.AdminDto;
import org.stadium.adminapi.service.dto.AlertResponseDto;
import org.stadium.adminapi.service.dto.JWTTokenDto;
import org.stadium.corelib.domain.Admin;
import org.stadium.corelib.domain.Role;
import org.stadium.corelib.repo.AdminRoleRepository;
import org.stadium.corelib.repo.admin.AdminRepository;
import org.stadium.adminapi.service.dto.RoleDto;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceImpl authService;
    private final HttpServletRequest httpServletRequest;

    public AdminServiceImpl(AdminRepository adminRepository,
                            AdminRoleRepository adminRoleRepository,
                            AdminMapper adminMapper, PasswordEncoder passwordEncoder,
                            AuthServiceImpl authService, HttpServletRequest httpServletRequest) {
        this.adminRepository = adminRepository;
        this.adminRoleRepository = adminRoleRepository;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Page<AdminDto> findAll(Pageable pageable) {
        Page<AdminDto> admins = adminRepository.findAll(pageable).map(adminMapper::toDto);
        return admins;
    }

    @Override
    public AdminDto findOne(Long id) throws BadRequestAlertException {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isEmpty())
            throw new BadRequestAlertException("Admin not found", "Admin", "Id", HttpStatus.NOT_FOUND);
        return adminRepository.findById(id).map(adminMapper::toDto).get();
    }

    @Override
    public JWTTokenDto save(AdminDto adminDto) throws Exception {
        Admin admin = adminMapper.toEntity(adminDto);
        boolean existsByUsername = adminRepository.existsByUsername(adminDto.getUsername());
        Set<Role> roles = adminRoleRepository.findDistinctBySetOfIds(adminDto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toSet()));

        if (!adminDto.getConfirmPassword().equals(adminDto.getPassword())) {
            throw new BadRequestAlertException("password and confirm password is not match", "Admin", "confirmPassword");
        }

        // create
        if (adminDto.getId() == null){
            if (existsByUsername){
                throw new BadRequestAlertException("Username already exits", "Admin", "username");
            }

            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            admin.setRoles(roles);
            adminRepository.save(admin);
            JWTTokenDto jwtTokenDto = authService.loginUser(httpServletRequest,new LoginVM(adminDto.getUsername(), adminDto.getPassword()));
            return jwtTokenDto;
        }

        // update
        boolean usernameAndIdNot = adminRepository.existsByUsernameAndIdNot(adminDto.getUsername(), admin.getId());
        if (usernameAndIdNot){
            throw new BadRequestAlertException("Username already exits", "Admin", "username");
        }

        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setRoles(roles);
        adminRepository.save(admin);
        JWTTokenDto jwtTokenDto = authService.loginUser(httpServletRequest, new LoginVM(adminDto.getUsername(), adminDto.getPassword()));
        return jwtTokenDto;
    }


    @Override
    public AlertResponseDto delete(Long id) throws Exception {
        if (id != null) {
            adminRepository.deleteById(id);
            return new AlertResponseDto("FieldManager is deleted", true);
        }

        return new AlertResponseDto("FieldManager is not deleted", false);
    }
}
