package com.flacko.card.service;

import com.flacko.common.exception.CardNotFoundException;

public interface CardService {

    CardBuilder create();

    CardBuilder update(String id) throws CardNotFoundException;

    CardListBuilder list();

    Card get(String id) throws CardNotFoundException;

}
