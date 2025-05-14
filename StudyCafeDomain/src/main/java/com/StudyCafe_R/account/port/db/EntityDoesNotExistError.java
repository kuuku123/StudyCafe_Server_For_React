package com.StudyCafe_R.account.port.db;

public class EntityDoesNotExistError extends PersistenceError {
    public EntityDoesNotExistError(String message) {
        super(message);
    }
}
