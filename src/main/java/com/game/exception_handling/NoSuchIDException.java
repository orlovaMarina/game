package com.game.exception_handling;

public class NoSuchIDException extends RuntimeException{
    public NoSuchIDException(String message) {
        super(message);
    }
}
