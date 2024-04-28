package com.flacko.card.service.exception;

import java.util.Optional;

public class CardMissingRequiredAttributeException extends Exception {

    public CardMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super(String.format("Missing required %s attribute for card %s", attributeName, id.orElse("unknown")));
    }

}
