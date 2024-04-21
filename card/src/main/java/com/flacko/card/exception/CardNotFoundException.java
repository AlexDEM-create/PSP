package com.flacko.card.exception;

import com.flacko.auth.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {

    public CardNotFoundException(String id) {
        super(String.format("Card %s not found", id));
    }

}
