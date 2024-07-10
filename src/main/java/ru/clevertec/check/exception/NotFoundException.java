package ru.clevertec.check.exception;

public class NotFoundException extends Exception {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
