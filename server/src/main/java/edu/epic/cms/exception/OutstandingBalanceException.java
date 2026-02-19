package edu.epic.cms.exception;

public class OutstandingBalanceException extends RuntimeException {
    public OutstandingBalanceException(String message) {
        super(message);
    }
}
