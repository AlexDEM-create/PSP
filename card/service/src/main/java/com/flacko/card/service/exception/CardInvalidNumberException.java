package com.flacko.card.service.exception;

public class CardInvalidNumberException extends Exception {

    public CardInvalidNumberException(String id, String number) {
        super(String.format("Invalid number %s provided for card %s", number, id));
    }

}
