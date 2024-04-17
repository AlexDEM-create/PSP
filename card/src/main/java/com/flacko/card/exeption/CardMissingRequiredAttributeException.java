package com.flacko.card.exception;

import java.util.Optional;

public class CardMissingRequiredAttributeException extends Exception {
    public CardMissingRequiredAttributeException(String attributeName, Optional<String> id) {
        super("Missing required " + attributeName + " attribute for card " + id.orElse("unknown"));
    }
}
