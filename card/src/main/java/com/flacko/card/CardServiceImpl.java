package com.flacko.card;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.card.exception.CardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
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

    @Override
    public CardBuilder create() {
        return serviceLocator.create(InitializableCardBuilder.class)
                .initializeNew();
    }

    @Override
    public CardBuilder update(String id) throws CardNotFoundException {
        Card existingCard = get(id);
        return serviceLocator.create(InitializableCardBuilder.class)
                .initializeExisting(existingCard);
    }

}
