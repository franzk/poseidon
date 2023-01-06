package com.nnk.springboot.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BidListNotFoundException.class)
    protected ResponseEntity<Object> handleBidListNotFoundException(BidListNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "BidList not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CurvePointNotFoundException.class)
    protected ResponseEntity<Object> handleCurvePointNotFoundException(CurvePointNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "CurvePoint not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RatingNotFoundException.class)
    protected ResponseEntity<Object> handleRatingNotFoundException(RatingNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Rating not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuleNameNotFoundException.class)
    protected ResponseEntity<Object> handleRuleNameNotFoundException(RuleNameNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "RuleName not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TradeNotFoundException.class)
    protected ResponseEntity<Object> handleTradeNotFoundException(TradeNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Trade not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "User not found !!", new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) throws IOException {
        List<String> messages = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource("messages.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        for (ConstraintViolation cv : ex.getConstraintViolations()) {
            String key = cv.getMessage().replace("{", "").replace("}", "");
            String message = props.getProperty(key, key);
            messages.add(message);
        }

        String message = String.join("\n", messages);
        return handleExceptionInternal(ex, message, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
