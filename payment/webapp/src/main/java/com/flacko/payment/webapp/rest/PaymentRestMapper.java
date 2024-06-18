package com.flacko.payment.webapp.rest;

import com.flacko.payment.service.Payment;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class PaymentRestMapper {

    PaymentResponse mapModelToResponse(Payment payment) {
        return new PaymentResponse(payment.getId(),
                payment.getMerchantId(),
                payment.getTraderTeamId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getBank(),
                payment.getCurrentState(),
                payment.getCreatedDate().atZone(ZoneId.systemDefault()),
                payment.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
