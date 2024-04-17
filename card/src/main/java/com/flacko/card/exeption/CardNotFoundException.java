package com.flacko.card.exception;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(String id) {
        super(String.format("Card %s not found", id));
    }
}