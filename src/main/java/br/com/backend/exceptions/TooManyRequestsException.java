package br.com.backend.exceptions;

public class TooManyRequestsException extends RuntimeException{
    public TooManyRequestsException(String message) {
        super(message);
    }
}
