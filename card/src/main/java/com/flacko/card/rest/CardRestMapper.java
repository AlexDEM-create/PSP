package com.flacko.card.rest;

import com.flacko.card.Card;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class CardRestMapper {
    CardResponse mapModelToResponse(Card card) {
        String cardNumber = card.getCardNumber();
        String maskedCardNumber = cardNumber.length() > 4 ? "**** **** **** " +
                cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        CardResponse cardResponse = new CardResponse(card.getCardId(),
                card.getCardName(),
                maskedCardNumber,
                card.getCreatedDate().atZone(ZoneId.systemDefault()),
                card.getUpdatedDate().atZone(ZoneId.systemDefault()));
        return cardResponse;
    }
}