package com.flacko.card;

import com.flacko.bank.exception.BankNotFoundException;
import com.flacko.card.exception.CardInvalidNumberException;
import com.flacko.card.exception.CardMissingRequiredAttributeException;
import com.flacko.trader.team.exception.TraderTeamNotFoundException;

public interface CardBuilder {

    CardBuilder withNumber(String number);

    CardBuilder withBankId(String bankId);

    CardBuilder withTraderTeamId(String traderTeamId);

    CardBuilder withBusy(boolean busy);

    CardBuilder withArchived();

    Card build() throws CardMissingRequiredAttributeException, TraderTeamNotFoundException, CardInvalidNumberException, BankNotFoundException;

}
