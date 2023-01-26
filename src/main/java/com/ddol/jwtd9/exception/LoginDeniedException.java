package com.ddol.jwtd9.exception;

public class LoginDeniedException extends RuntimeException {
    public LoginDeniedException() {
        super();
    }

    public LoginDeniedException(String message) {
        super(message);
    }

    public LoginDeniedException(Throwable cause) {
        super(cause);
    }

    public LoginDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
