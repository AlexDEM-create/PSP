package com.flacko.outgoing.payment.webapp.rest;

import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.PaymentDirection;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public record OutgoingPaymentFilterRequest(@RequestParam(MERCHANT_ID) Optional<String> merchantId,
                                           @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                           @RequestParam(CARD_ID) Optional<String> cardId,
                                           @RequestParam(DIRECTION) Optional<PaymentDirection> direction,
                                           @RequestParam(CURRENT_STATE) Optional<PaymentState> currentState) {

    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String CARD_ID = "card_id";
    private static final String DIRECTION = "direction";
    private static final String CURRENT_STATE = "current_state";

}
