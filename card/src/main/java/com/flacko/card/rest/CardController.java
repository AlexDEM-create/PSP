package com.flacko.card.rest;

import com.flacko.bank.exception.BankNotFoundException;
import com.flacko.card.Card;
import com.flacko.card.CardBuilder;
import com.flacko.card.CardService;
import com.flacko.card.exception.CardInvalidNumberException;
import com.flacko.card.exception.CardMissingRequiredAttributeException;
import com.flacko.card.exception.CardNotFoundException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;
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
    public List<CardResponse> list() {
        return cardService.list()
                .stream()
                .map(cardRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{cardId}")
    public CardResponse get(@PathVariable String cardId) throws CardNotFoundException {
        return cardRestMapper.mapModelToResponse(cardService.get(cardId));
    }

    @PostMapping
    public CardResponse create(@RequestBody CardCreateRequest cardCreateRequest)
            throws CardMissingRequiredAttributeException, TraderTeamNotFoundException, CardInvalidNumberException,
            BankNotFoundException {
        CardBuilder builder = cardService.create();
        builder.withNumber(cardCreateRequest.number())
                .withBankId(cardCreateRequest.bankId())
                .withTraderTeamId(cardCreateRequest.traderTeamId());
        Card card = builder.build();
        return cardRestMapper.mapModelToResponse(card);
    }

    @DeleteMapping("/{cardId}")
    public CardResponse archive(@PathVariable String cardId)
            throws CardNotFoundException, CardMissingRequiredAttributeException, TraderTeamNotFoundException,
            CardInvalidNumberException, BankNotFoundException {
        CardBuilder builder = cardService.update(cardId);
        builder.withArchived();
        Card card = builder.build();
        return cardRestMapper.mapModelToResponse(card);
    }

}