package org.stadium.adminapi.security;


import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
