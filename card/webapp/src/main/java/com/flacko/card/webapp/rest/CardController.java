package com.flacko.card.webapp.rest;

import com.flacko.card.service.Card;
import com.flacko.card.service.CardBuilder;
import com.flacko.card.service.CardListBuilder;
import com.flacko.card.service.CardService;
import com.flacko.card.service.exception.CardInvalidNumberException;
import com.flacko.card.service.exception.CardMissingRequiredAttributeException;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.CardNotFoundException;
import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final CardRestMapper cardRestMapper;

    @GetMapping
    public List<CardResponse> list(CardFilterRequest cardFilterRequest) {
        CardListBuilder builder = cardService.list();
        cardFilterRequest.bankId().ifPresent(builder::withBankId);
        cardFilterRequest.traderTeamId().ifPresent(builder::withTraderTeamId);
        cardFilterRequest.terminalId().ifPresent(builder::withTerminalId);
        cardFilterRequest.busy().ifPresent(builder::withBusy);
        return builder.build()
                .stream()
                .map(cardRestMapper::mapModelToResponse)
                .skip(cardFilterRequest.offset())
                .limit(cardFilterRequest.limit())
                .collect(Collectors.toList());
    }

    @GetMapping("/{cardId}")
    public CardResponse get(@PathVariable String cardId) throws CardNotFoundException {
        return cardRestMapper.mapModelToResponse(cardService.get(cardId));
    }

    @PostMapping
    public CardResponse create(@RequestBody CardCreateRequest cardCreateRequest)
            throws CardMissingRequiredAttributeException, TraderTeamNotFoundException, CardInvalidNumberException,
            BankNotFoundException, TerminalNotFoundException {
        CardBuilder builder = cardService.create();
        builder.withNumber(cardCreateRequest.number())
                .withBankId(cardCreateRequest.bankId())
                .withTraderTeamId(cardCreateRequest.traderTeamId())
                .withTerminalId(cardCreateRequest.terminalId());
        Card card = builder.build();
        return cardRestMapper.mapModelToResponse(card);
    }

    @DeleteMapping("/{cardId}")
    public CardResponse archive(@PathVariable String cardId)
            throws CardNotFoundException, CardMissingRequiredAttributeException, TraderTeamNotFoundException,
            CardInvalidNumberException, BankNotFoundException, TerminalNotFoundException {
        CardBuilder builder = cardService.update(cardId);
        builder.withArchived();
        Card card = builder.build();
        return cardRestMapper.mapModelToResponse(card);
    }

}