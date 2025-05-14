package com.StudyCafe_R.account;

public class GenericEnrollmentError extends RuntimeException {
    public GenericEnrollmentError(String message) {
        super(message);
    }

    public GenericEnrollmentError(String message, Throwable cause) {
        super(message, cause);
    }
}
