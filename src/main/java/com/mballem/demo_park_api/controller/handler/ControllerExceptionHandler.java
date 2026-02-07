package com.mballem.demo_park_api.controller.handler;

import com.mballem.demo_park_api.dto.ErrorMessage;
import com.mballem.demo_park_api.service.exception.PasswordInvalidException;
import com.mballem.demo_park_api.service.exception.ResorceNotFoundException;
import com.mballem.demo_park_api.service.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpServletRequest request, BindingResult result){

        log.error("Api error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalidos", result);

        // Unprocessable é o status 422
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> UsernameUniqueViolation(UsernameUniqueViolationException exception, HttpServletRequest request){

        log.error("Api error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.CONFLICT, exception.getMessage());

        // Conflict é o status 409
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);

    }

    @ExceptionHandler(ResorceNotFoundException.class)
    public ResponseEntity<ErrorMessage> ResorceNotFound(ResorceNotFoundException exception, HttpServletRequest request){

        log.error("Api error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.NOT_FOUND, exception.getMessage());

        // Not found é o 404
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);

    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> PasswordInvalid(PasswordInvalidException exception, HttpServletRequest request){

        log.error("Api error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.BAD_REQUEST, exception.getMessage());

        // Bad request é o 400
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);

    }

}
