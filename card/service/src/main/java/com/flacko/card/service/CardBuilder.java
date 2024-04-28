package com.flacko.card.service;

import com.flacko.card.service.exception.CardInvalidNumberException;
import com.flacko.card.service.exception.CardMissingRequiredAttributeException;
import com.flacko.common.exception.BankNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;

public interface CardBuilder {

    CardBuilder withNumber(String number);

    CardBuilder withBankId(String bankId);

    CardBuilder withTraderTeamId(String traderTeamId);

    CardBuilder withBusy(boolean busy);

    CardBuilder withArchived();

    Card build() throws CardMissingRequiredAttributeException, TraderTeamNotFoundException, CardInvalidNumberException,
            BankNotFoundException;

}
