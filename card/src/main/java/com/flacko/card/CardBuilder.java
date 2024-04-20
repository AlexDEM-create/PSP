package com.flacko.card;

import com.flacko.card.exception.CardMissingRequiredAttributeException;

public interface CardBuilder {

    CardBuilder withNumber(String number);

    CardBuilder withBankId(String bankId);

    CardBuilder withTraderTeamId(String traderTeamId);

    CardBuilder withBusy(boolean busy);

    CardBuilder withArchived();

    Card build() throws CardMissingRequiredAttributeException;

}
