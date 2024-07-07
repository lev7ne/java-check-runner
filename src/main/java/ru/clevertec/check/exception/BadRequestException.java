package ru.clevertec.check.exception;

public class BadRequestException extends RuntimeException {
    private String path;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, String path) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
