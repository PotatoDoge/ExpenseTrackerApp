package com.expensetrackerapp.shared;

import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalTime.now;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(NullRequestException.class)
    public ResponseEntity<CustomResponse> handleSendingEmailErrorException(NullRequestException ex) {
        log.error("NullRequestException occurred: ", ex);
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DatabaseInteractionException.class)
    public ResponseEntity<CustomResponse> handleDatabaseInteractionException(DatabaseInteractionException ex) {
        log.error("DatabaseInteractionException occurred: ", ex);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<CustomResponse> handleMappingException(MappingException ex) {
        log.error("MappingException occurred: ", ex);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(NotFoundInDatabase.class)
    public ResponseEntity<CustomResponse> handleNotFoundInDatabase(NotFoundInDatabase ex) {
        log.error("NotFoundInDatabase occurred: ", ex);
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<CustomResponse> createResponse(HttpStatus status, String message) {
        CustomResponse response = CustomResponse.builder()
                .timestamp(now().toString())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
