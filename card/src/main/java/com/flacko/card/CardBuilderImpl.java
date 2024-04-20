package com.flacko.card;

import com.flacko.auth.id.IdGenerator;
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

    private final CardRepository cardRepository;

    private CardPojo.CardPojoBuilder pojoBuilder;

    @Override
    public CardBuilder initializeNew() {
        pojoBuilder = CardPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public CardBuilder initializeExisting(Card existingCard) {
        pojoBuilder = CardPojo.builder()
                .primaryKey(existingCard.getPrimaryKey())
                .id(existingCard.getId())
                .number(existingCard.getNumber())
                .bankId(existingCard.getBankId())
                .traderTeamId(existingCard.getTraderTeamId())
                .busy(existingCard.isBusy())
                .createdDate(existingCard.getCreatedDate())
                .updatedDate(now)
                .deletedDate(existingCard.getDeletedDate().orElse(null));
        return this;
    }

    @Override
    public CardBuilder withNumber(String number) {
        pojoBuilder.number(number);
        return this;
    }

    @Override
    public CardBuilder withBankId(String id) {
        pojoBuilder.bankId(id);
        return this;
    }

    @Override
    public CardBuilder withTraderTeamId(String traderTeamId) {
        pojoBuilder.traderTeamId(traderTeamId);
        return this;
    }

    @Override
    public CardBuilder withBusy(boolean busy) {
        pojoBuilder.busy(busy);
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
        cardRepository.save(card);
        return card;
    }

    private void validate(CardPojo pojo) throws CardMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new CardMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getNumber() == null || pojo.getNumber().isEmpty()) {
            throw new CardMissingRequiredAttributeException("number", Optional.empty());
        }
        if (pojo.getBankId() == null || pojo.getBankId().isEmpty()) {
            throw new CardMissingRequiredAttributeException("bankId", Optional.empty());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isEmpty()) {
            throw new CardMissingRequiredAttributeException("traderTeamId", Optional.empty());
        }
    }

}
