package com.flacko.card;

import com.flacko.card.Card;

import java.time.Instant;

public interface CardBuilder {

    CardBuilder withCardId(String id);

    CardBuilder withCardNumber(String number);

    CardBuilder withCardName(String name);

    CardBuilder withCardDate(Instant date);

    CardBuilder withBankId(String id);

    CardBuilder withActive(boolean isActive);

    CardBuilder withTraderId(String id);

    CardBuilder withArchived();

    Card build() throws com.flacko.card.exception.CardMissingRequiredAttributeException;

}