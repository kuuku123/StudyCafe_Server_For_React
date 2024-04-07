package com.StudyCafe_R.modules.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.StudyCafe_R.modules.account")
public class AccountExceptionAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult accountServerExceptionHandle(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResult("bullshit", ex.getMessage());
    }
}
