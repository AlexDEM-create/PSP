package com.flacko.outgoing.payment.webapp.rest;

import com.flacko.payment.service.Payment;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class OutgoingPaymentRestMapper {

    OutgoingPaymentResponse mapModelToResponse(Payment payment) {
        // add timezone from authorization
        return new OutgoingPaymentResponse(payment.getId(),
                payment.getMerchantId(),
                payment.getTraderTeamId(),
                payment.getCardId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDirection(),
                payment.getCurrentState(),
                payment.getCreatedDate().atZone(ZoneId.systemDefault()),
                payment.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
