package com.expensetrackerapp.shared;

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

    private ResponseEntity<CustomResponse> createResponse(HttpStatus status, String message) {
        CustomResponse response = CustomResponse.builder()
                .timestamp(now().toString())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
