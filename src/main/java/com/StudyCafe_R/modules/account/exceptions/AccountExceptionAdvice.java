package com.StudyCafe_R.modules.account.exceptions;

import com.StudyCafe_R.modules.account.ErrorResult;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.StudyCafe_R.modules.account")
public class AccountExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> accountServerIllegalArgumentExceptionHandle(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse<ByteArrayResource> apiResponse = new ApiResponse<>(ex.getMessage(), HttpStatus.BAD_REQUEST,null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> accountServerExceptionHandle(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiResponse<ByteArrayResource> apiResponse = new ApiResponse<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
