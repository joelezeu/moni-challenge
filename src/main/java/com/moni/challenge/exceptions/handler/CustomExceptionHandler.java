package com.moni.challenge.exceptions.handler;


import com.moni.challenge.domain.dto.Response;
import com.moni.challenge.exceptions.ChallengeException;
import com.moni.challenge.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final ResponseUtils responseUtils;
    @ExceptionHandler(ChallengeException.class)
    public ResponseEntity<Response> handleCustomNotFoundException(ChallengeException ex) {
        return new ResponseEntity<>( responseUtils.getCustomerResponse(false, ex.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }
}
