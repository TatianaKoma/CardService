package com.greedobank.cards.exception;

public class NotActiveException extends RuntimeException {
    public NotActiveException(String message) {
        super(message);
    }
}
