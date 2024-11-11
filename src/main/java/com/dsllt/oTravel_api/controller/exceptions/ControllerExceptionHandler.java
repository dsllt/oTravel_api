package com.dsllt.oTravel_api.controller.exceptions;

import com.dsllt.oTravel_api.service.exceptions.EmailAlreadyExistsException;
import com.dsllt.oTravel_api.service.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.service.exceptions.PlaceAlreadyExistsException;
import com.dsllt.oTravel_api.service.exceptions.TokenVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<StandardError> emailAlreadyExistsError(EmailAlreadyExistsException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(Instant.now(), status.value(), "Email já cadastrado.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(PlaceAlreadyExistsException.class)
    public ResponseEntity<StandardError> placeAlreadyExistsError(PlaceAlreadyExistsException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(Instant.now(), status.value(), "Lugar já cadastrado.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> httpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(Instant.now(), status.value(), "Valores inválidos no JSON.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {
        BindingResult result = e.getBindingResult();

        ArrayList<String> errorMessages = new ArrayList<>();

        result.getFieldErrors().forEach(fieldError -> {
            errorMessages.add(fieldError.getDefaultMessage());
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String requestUri = request.getDescription(false);
        if (requestUri.startsWith("uri=")) {
            requestUri = requestUri.substring(4);
        }

        ValidationError err = new ValidationError(Instant.now(), status.value(), "Erro ao validar dados.", errorMessages,
                requestUri);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundError(ObjectNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError err = new StandardError(Instant.now(), status.value(), "Objeto não encontrado.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> argumentTypeMismatchError(MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(Instant.now(), status.value(), "Erro na validação dos valores recebidos.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(TokenVerificationException.class)
    public ResponseEntity<StandardError> handleTokenVerificationException(TokenVerificationException e,
                                                                          HttpServletRequest request) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError err = new StandardError(Instant.now(), status.value(), "Erro ao verificar token.", e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

}
