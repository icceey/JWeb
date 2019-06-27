package com.icceey.jweb.exception;

import com.icceey.jweb.beans.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse allExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("---------------------------->Exception");
        log.error(e.getMessage());
        return BaseResponse.exception(e.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public BaseResponse constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        Set<ConstraintViolation<?>> es = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : es) {
            log.error(constraintViolation.getMessage());
        }
        return BaseResponse.exception();
    }




}
