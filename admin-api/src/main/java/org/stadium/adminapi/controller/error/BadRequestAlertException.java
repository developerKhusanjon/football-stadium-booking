package org.stadium.adminapi.controller.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
public class BadRequestAlertException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;
    private final String message;
    private final LocalDateTime time = LocalDateTime.now();
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadRequestAlertException(String message, String entityName, String errorKey) {
        this.message = message;
        this.entityName = entityName;
        this.errorKey = errorKey;
    }
    public BadRequestAlertException(String message, String entityName, String errorKey, HttpStatus status) {
        this.message = message;
        this.entityName = entityName;
        this.errorKey = errorKey;
        this.status = status;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
