package com.flacko.payment.webapp.outgoing.rest;

import com.flacko.payment.service.outgoing.OutgoingPayment;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class OutgoingPaymentRestMapper {

    OutgoingPaymentResponse mapModelToResponse(OutgoingPayment outgoingPayment) {
        // add timezone from authorization
        return new OutgoingPaymentResponse(outgoingPayment.getId(),
                outgoingPayment.getMerchantId(),
                outgoingPayment.getTraderTeamId(),
                outgoingPayment.getPaymentMethodId(),
                outgoingPayment.getAmount(),
                outgoingPayment.getCurrency(),
                outgoingPayment.getRecipient(),
                outgoingPayment.getBank(),
                outgoingPayment.getRecipientPaymentMethodType(),
                outgoingPayment.getPartnerPaymentId(),
                outgoingPayment.getCurrentState(),
                outgoingPayment.getCreatedDate().atZone(ZoneId.systemDefault()),
                outgoingPayment.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

    OutgoingPaymentCreateResponse mapModelToCreateResponse(OutgoingPayment outgoingPayment) {
        return new OutgoingPaymentCreateResponse(outgoingPayment.getId());
    }

}
