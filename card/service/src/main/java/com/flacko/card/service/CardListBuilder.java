package com.flacko.card.service;

import java.util.List;

public interface CardListBuilder {

    CardListBuilder withBankId(String bankId);

    CardListBuilder withTraderTeamId(String traderTeamId);

    CardListBuilder withTerminalId(String terminalId);

    CardListBuilder withBusy(boolean busy);

    List<Card> build();

}
