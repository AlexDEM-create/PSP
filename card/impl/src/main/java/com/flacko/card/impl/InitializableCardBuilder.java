package com.flacko.card.impl;

import com.flacko.card.service.Card;
import com.flacko.card.service.CardBuilder;

public interface InitializableCardBuilder extends CardBuilder {

    CardBuilder initializeNew();

    CardBuilder initializeExisting(Card existingCard);

}
