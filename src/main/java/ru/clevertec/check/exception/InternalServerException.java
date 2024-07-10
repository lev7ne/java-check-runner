package ru.clevertec.check.exception;

public class InternalServerException extends Exception {
    public InternalServerException() {
    }

    public InternalServerException(String message) {
        super(message);
    }
}
