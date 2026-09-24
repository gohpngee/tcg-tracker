package com.tcgtracker.common.exception.classes;

public class NoCardsFoundException extends RuntimeException {
    public NoCardsFoundException(String message) {
        super(message);
    }
}