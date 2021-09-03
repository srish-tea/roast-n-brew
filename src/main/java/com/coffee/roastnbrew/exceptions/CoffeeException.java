package com.coffee.roastnbrew.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoffeeException extends Exception {
    
    public CoffeeException() {
        super("Unknown Error");
    }
    
    public CoffeeException(String message) {
        super(message);
    }
    
    public CoffeeException(String message, Throwable cause) {
        super(message, cause);
        
        if(cause instanceof InterruptedException) {
            log.debug("it was an interrupted exception. interrupting thread again.");
            Thread.currentThread().interrupt();
        }
    }
    
    public CoffeeException(Throwable cause) {
        super(cause);
        
        if(cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
