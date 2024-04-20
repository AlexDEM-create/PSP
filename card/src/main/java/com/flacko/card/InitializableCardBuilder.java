package com.flacko.card;

public interface InitializableCardBuilder extends CardBuilder {

    CardBuilder initializeNew();

    CardBuilder initializeExisting(Card existingCard);
}
