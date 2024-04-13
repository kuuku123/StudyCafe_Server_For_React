package com.StudyCafe_R.modules.account.exceptions;

import com.StudyCafe_R.modules.account.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.StudyCafe_R.modules.account")
public class AccountExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult accountServerIllegalArgumentExceptionHandle(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
            return new ErrorResult(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResult accountServerExceptionHandle(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
