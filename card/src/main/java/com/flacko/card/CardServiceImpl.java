package com.flacko.card;

import com.flacko.card.exception.CardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
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
    private final ApplicationContext context;
    @Override
    public CardBuilder create() {
        return context.getBean(CardBuilderImpl.class)
                .initializeNew();
    }

    @Override
    public CardBuilder update(String id) throws CardNotFoundException {
        Card existingCard = (Card)get(id);
        return context.getBean(CardBuilderImpl.class)
                .initializeExisting(existingCard);
    }

    @Override
    public CardBuilder get(String id) throws CardNotFoundException {
        return context.getBean(CardBuilderImpl.class)
                .initializeExisting(cardRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new CardNotFoundException(id)));
    }

    @Override
    public List<CardBuilder> list() {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false)
                .map(card -> context.getBean(CardBuilderImpl.class)
                        .initializeExisting(card))
                .collect(Collectors.toList());
    }
}
