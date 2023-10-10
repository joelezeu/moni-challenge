package com.moni.challenge.exceptions;

import com.moni.challenge.domain.dto.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ChallengeException extends RuntimeException{
    private String errorMessage;
    private HttpStatus httpStatus;

    private Response response;

    public ChallengeException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
