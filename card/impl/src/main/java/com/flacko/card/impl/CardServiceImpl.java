package com.flacko.card.impl;

import com.flacko.card.service.Card;
import com.flacko.card.service.CardBuilder;
import com.flacko.card.service.CardService;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Card> list() {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Card get(String id) throws CardNotFoundException {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    @Transactional
    @Override
    public CardBuilder create() {
        return serviceLocator.create(InitializableCardBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public CardBuilder update(String id) throws CardNotFoundException {
        Card existingCard = get(id);
        return serviceLocator.create(InitializableCardBuilder.class)
                .initializeExisting(existingCard);
    }

}
