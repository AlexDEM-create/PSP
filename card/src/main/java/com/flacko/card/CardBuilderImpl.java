package com.flacko.card;

import com.flacko.card.exception.CardMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CardBuilderImpl implements InitializableCardBuilder {

    private final Instant now = Instant.now();

    private CardPojo.CardPojoBuilder pojoBuilder;

    @Override
    public CardBuilder initializeNew() {
        pojoBuilder = CardPojo.builder()
                .createdDate(now)
                .updatedDate(now);
        return this;
    }

    @Override
    public CardBuilder initializeExisting(Card existingCard) {
        pojoBuilder = CardPojo.builder()
                .cardId(existingCard.getCardId())
                .cardNumber(existingCard.getCardNumber())
                .cardName(existingCard.getCardName())
                .cardDate(existingCard.getCardDate())
                .bankId(existingCard.getBankId())
                .isActive(existingCard.isActive())
                .traderId(existingCard.getTraderId())
                .createdDate(existingCard.getCreatedDate())
                .updatedDate(now);
        return this;
    }

    @Override
    public CardBuilder withCardId(String id) {
        pojoBuilder.cardId(id);
        return this;
    }

    @Override
    public CardBuilder withCardNumber(String number) {
        pojoBuilder.cardNumber(number);
        return this;
    }

    @Override
    public CardBuilder withCardName(String name) {
        pojoBuilder.cardName(name);
        return this;
    }

    @Override
    public CardBuilder withCardDate(Instant date) {
        pojoBuilder.cardDate(date);
        return this;
    }

    @Override
    public CardBuilder withBankId(String id) {
        pojoBuilder.bankId(id);
        return this;
    }

    @Override
    public CardBuilder withActive(boolean isActive) {
        pojoBuilder.isActive(isActive);
        return this;
    }

    @Override
    public CardBuilder withTraderId(String id) {
        pojoBuilder.traderId(id);
        return this;
    }

    @Override
    public CardBuilder withArchived() {
        pojoBuilder.deletedDate(now);
        return this;
    }

    @Override
    public Card build() throws CardMissingRequiredAttributeException {
        CardPojo card = pojoBuilder.build();
        validate(card);
        return card;
    }

    private void validate(CardPojo card) throws CardMissingRequiredAttributeException {
        if (card.getCardId() == null || card.getCardId().isEmpty()) {
            throw new CardMissingRequiredAttributeException("cardId", Optional.empty());
        }
        //... add all the necessary validations here for the other fields like cardName, cardNumber, etc
    }
}

