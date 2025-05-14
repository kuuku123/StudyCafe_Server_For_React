package com.StudyCafe_R.account.port.db;

import com.StudyCafe_R.account.GenericEnrollmentError;

public class PersistenceError extends GenericEnrollmentError {
    public PersistenceError(String message) {
        super(message);
    }

    public PersistenceError(String message, Throwable cause) {
        super(message, cause);
    }
}
