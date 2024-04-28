package com.flacko.common.exception;

public class CardNotFoundException extends NotFoundException {

    public CardNotFoundException(String id) {
        super(String.format("Card %s not found", id));
    }

}
