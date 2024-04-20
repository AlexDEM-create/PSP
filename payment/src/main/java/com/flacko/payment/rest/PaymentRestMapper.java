package com.flacko.payment.rest;

import com.flacko.payment.Payment;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class PaymentRestMapper {

    PaymentResponse mapModelToResponse(Payment payment) {
        // add timezone from authorization
        return new PaymentResponse(payment.getId(),
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
