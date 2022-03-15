package com.company.controller;

import com.company.exception.BadRequestException;
import com.company.exception.ForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.exception.UnauthorizaedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({ItemNotFoundException.class, BadRequestException.class})
    public ResponseEntity handleException(RuntimeException e){
        e.printStackTrace();
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity handleException(ForbiddenException e){
        e.printStackTrace();
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler({UnauthorizaedException.class})
    public ResponseEntity handleException(UnauthorizaedException e){
        e.printStackTrace();
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
