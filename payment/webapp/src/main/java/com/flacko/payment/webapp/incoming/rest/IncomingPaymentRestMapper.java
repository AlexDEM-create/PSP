package com.flacko.payment.webapp.incoming.rest;

import com.flacko.payment.service.incoming.IncomingPayment;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class IncomingPaymentRestMapper {

    IncomingPaymentResponse mapModelToResponse(IncomingPayment incomingPayment) {
        // add timezone from authorization
        return new IncomingPaymentResponse(incomingPayment.getId(),
                incomingPayment.getMerchantId(),
                incomingPayment.getTraderTeamId(),
                incomingPayment.getPaymentMethodId(),
                incomingPayment.getAmount(),
                incomingPayment.getCurrency(),
                incomingPayment.getCurrentState(),
                incomingPayment.getCreatedDate().atZone(ZoneId.systemDefault()),
                incomingPayment.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
