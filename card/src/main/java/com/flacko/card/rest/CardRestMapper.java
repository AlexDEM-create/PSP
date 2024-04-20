package com.flacko.card.rest;

import com.flacko.card.Card;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class CardRestMapper {

    CardResponse mapModelToResponse(Card card) {
        String number = card.getNumber();
        String maskedNumber = "**** **** **** " + number.substring(number.length() - 4);
        return new CardResponse(card.getId(),
                maskedNumber,
                card.getBankId(),
                card.getTraderTeamId(),
                card.isBusy(),
                card.getCreatedDate().atZone(ZoneId.systemDefault()),
                card.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}