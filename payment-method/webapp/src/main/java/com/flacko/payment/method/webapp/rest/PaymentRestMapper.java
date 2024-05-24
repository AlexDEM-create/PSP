package com.flacko.payment.method.webapp.rest;

import com.flacko.payment.method.service.PaymentMethod;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class PaymentRestMapper {

    PaymentMethodResponse mapModelToResponse(PaymentMethod paymentMethod) {
        return new PaymentMethodResponse(paymentMethod.getId(),
                paymentMethod.getNumber(),
                paymentMethod.getFirstName(),
                paymentMethod.getLastName(),
                paymentMethod.getCurrency(),
                paymentMethod.getBank(),
                paymentMethod.getTraderTeamId(),
                paymentMethod.getTerminalId(),
                paymentMethod.isEnabled(),
                paymentMethod.isBusy(),
                paymentMethod.getCreatedDate().atZone(ZoneId.systemDefault()),
                paymentMethod.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
