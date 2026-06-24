package com.online.ContactBook.exception;

public class InvalidCaptchaException extends RuntimeException{
    public InvalidCaptchaException(String message) {
        super(message);
    }
}
