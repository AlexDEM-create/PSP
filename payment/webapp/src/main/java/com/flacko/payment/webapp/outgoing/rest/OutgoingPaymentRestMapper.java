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
                outgoingPayment.getCurrentState(),
                outgoingPayment.isBooked(),
                outgoingPayment.getCreatedDate().atZone(ZoneId.systemDefault()),
                outgoingPayment.getUpdatedDate().atZone(ZoneId.systemDefault()),
                outgoingPayment.getBookedDate().map(bookedDate -> bookedDate.atZone(ZoneId.systemDefault())));
    }

}
