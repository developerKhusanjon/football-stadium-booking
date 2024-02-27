package org.stadium.adminapi.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stadium.corelib.domain.Admin;
import org.stadium.corelib.repo.admin.AdminRepository;

@Service
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final AdminRepository adminRepository;

    public DomainUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional
    public Admin loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        return adminRepository.findByUsername(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UserNotFoundException("User with username " + login + " was not found in the database"));

    }

    public Admin loadAdminsId(Long id) {
        return adminRepository.findById(id)
                .map(user -> createSpringSecurityUser(String.valueOf(id), user))
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + id + " was not found in the database"));
    }

    private Admin createSpringSecurityUser(String login, Admin user) {
        if (!user.isEnabled()) {
            throw new UserNotFoundException("User " + login + " was not activated");
        }

        return user;
    }
}
