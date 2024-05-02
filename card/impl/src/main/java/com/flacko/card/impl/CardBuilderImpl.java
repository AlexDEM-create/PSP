package com.flacko.card.impl;

import com.flacko.bank.service.BankService;
import com.flacko.card.service.Card;
import com.flacko.card.service.CardBuilder;
import com.flacko.card.service.exception.CardInvalidNumberException;
import com.flacko.card.service.exception.CardMissingRequiredAttributeException;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CardBuilderImpl implements InitializableCardBuilder {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");

    private final Instant now = Instant.now();

    private final CardRepository cardRepository;
    private final BankService bankService;
    private final TraderTeamService traderTeamService;
    private final TerminalService terminalService;

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
    public CardBuilder withTerminalId(String terminalId) {
        pojoBuilder.terminalId(terminalId);
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
    public Card build() throws CardMissingRequiredAttributeException, TraderTeamNotFoundException,
            CardInvalidNumberException, BankNotFoundException, TerminalNotFoundException {
        CardPojo card = pojoBuilder.build();
        validate(card);
        cardRepository.save(card);
        return card;
    }

    private void validate(CardPojo pojo) throws CardMissingRequiredAttributeException, BankNotFoundException,
            TraderTeamNotFoundException, CardInvalidNumberException, TerminalNotFoundException {
        if (pojo.getId() == null || pojo.getId().isBlank()) {
            throw new CardMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getNumber() == null || pojo.getNumber().isBlank()) {
            throw new CardMissingRequiredAttributeException("number", Optional.empty());
        } else if (!CARD_NUMBER_PATTERN.matcher(pojo.getNumber()).matches()) {
            throw new CardInvalidNumberException(pojo.getId(), pojo.getNumber());
        }
        if (pojo.getBankId() == null || pojo.getBankId().isBlank()) {
            throw new CardMissingRequiredAttributeException("bankId", Optional.empty());
        } else {
            bankService.get(pojo.getBankId());
        }
        if (pojo.getTraderTeamId() == null || pojo.getTraderTeamId().isBlank()) {
            throw new CardMissingRequiredAttributeException("traderTeamId", Optional.empty());
        } else {
            traderTeamService.get(pojo.getTraderTeamId());
        }
        if (pojo.getTerminalId() == null || pojo.getTerminalId().isBlank()) {
            throw new CardMissingRequiredAttributeException("terminalId", Optional.empty());
        } else {
            terminalService.get(pojo.getTerminalId());
        }
    }

}
