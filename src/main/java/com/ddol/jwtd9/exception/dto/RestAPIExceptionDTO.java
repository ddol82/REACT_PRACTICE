package com.ddol.jwtd9.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class RestAPIExceptionDTO {
    private int state;
    private String message;

    public RestAPIExceptionDTO(HttpStatus httpStatus, String message) {
        this.state = httpStatus.value();
        this.message = message;
    }
}
