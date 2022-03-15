package com.company.exception;

public class UnauthorizaedException extends RuntimeException{
    public UnauthorizaedException(String message) {
        super(message);
    }
}
