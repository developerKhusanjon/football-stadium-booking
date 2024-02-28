package org.stadium.userapi.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stadium.corelib.domain.User;
import org.stadium.corelib.domain.UserSession;
import org.stadium.corelib.repo.UserRepository;
import org.stadium.corelib.repo.UserSessionRepository;
import org.stadium.userapi.controller.errors.BadRequestAlertException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;


    public DomainUserDetailsService(UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        return userRepository.findUserByCredential(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + login + " was not found in the database"));

    }

    public UserDetails loadUserById(final Long userId, UserSession session) throws BadRequestAlertException {
        log.debug("Authenticating {}", userId);

        return userRepository.findById(userId)
                .map(user -> createSpringSecurityUser(String.valueOf(userId), user, session))
                .orElseThrow(() -> new BadRequestAlertException("User with userId " + userId + " was not found in the database", "user", "id", HttpStatus.NOT_FOUND));

    }

    public UserSession loadUserSessionByJwt(String token) throws BadRequestAlertException {

        return userSessionRepository.findByUserToken(token).orElseThrow(() -> new BadRequestAlertException("User session was not found in the database", "user", "id", HttpStatus.NOT_FOUND));
    }

    private User createSpringSecurityUser(String login, User user) {
        SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if(!user.isEnabled()) {
            throw new UserNotActivatedException("User " + login + " was not activated");
        }
        return user;
    }
    private User createSpringSecurityUser(String login, User user, UserSession session) {
        if(!user.isEnabled() && !session.isEnabled()) {
            throw new UserNotActivatedException("User " + login + " was not activated");
        }
        if (LocalDateTime.now().minusDays(1).isBefore(user.getLoginAt())){
            user.setLoginAt(LocalDateTime.now());
            userRepository.save(user);
        }
        return user;
    }
}

