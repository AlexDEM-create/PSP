package com.flacko.card;

import com.flacko.card.exception.CardNotFoundException;

import java.util.List;

public interface CardService {

    CardBuilder create();

    CardBuilder update(String id) throws CardNotFoundException;

    List<Card> list();

    Card get(String id) throws CardNotFoundException;

}
