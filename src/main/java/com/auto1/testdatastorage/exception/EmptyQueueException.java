package com.auto1.testdatastorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmptyQueueException extends RuntimeException {

    public EmptyQueueException(String message) {
        super(message);
    }

}
