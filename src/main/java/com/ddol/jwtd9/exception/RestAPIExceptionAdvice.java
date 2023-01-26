package com.ddol.jwtd9.exception;

import com.ddol.jwtd9.exception.dto.RestAPIExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestAPIExceptionAdvice {
    @ExceptionHandler(LoginDeniedException.class)
    public ResponseEntity<RestAPIExceptionDTO> exceptionHandler(LoginDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestAPIExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<RestAPIExceptionDTO> exceptionHandler(UserException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestAPIExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<RestAPIExceptionDTO> exceptionHandler(TokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new RestAPIExceptionDTO(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }
}
