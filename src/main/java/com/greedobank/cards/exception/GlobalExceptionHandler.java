package com.greedobank.cards.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.greedobank.cards.utils.ResponseMessages.RESOURCE_NOT_FOUND;
import static com.greedobank.cards.utils.ResponseMessages.VALIDATION_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        return new JsonErrorResponse(VALIDATION_ERROR.getDescription(), errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonErrorResponse handleNonReadable(HttpMessageNotReadableException exception) {
        return new JsonErrorResponse(VALIDATION_ERROR.getDescription(), Arrays.asList(exception.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public JsonErrorResponse handleNoHandlerFound() {
        return new JsonErrorResponse(RESOURCE_NOT_FOUND.getDescription());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public JsonErrorResponse handleNotFoundException(NotFoundException exception) {
        return new JsonErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonErrorResponse handleInvalidInputException(InvalidInputException exception) {
        return new JsonErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NotActiveException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonErrorResponse handleNotActiveException(NotActiveException exception) {
        return new JsonErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonErrorResponse handleInsufficientFundsException(InsufficientFundsException exception) {
        return new JsonErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = RestTemplateException.class)
    public JsonErrorResponse handleRestTemplateException(RestTemplateException exception) {
        return new JsonErrorResponse(exception.getMessage());
    }
}
