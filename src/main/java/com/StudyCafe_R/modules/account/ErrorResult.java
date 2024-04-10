package com.StudyCafe_R.modules.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResult {
    private HttpStatus status;
    private String body;
}
