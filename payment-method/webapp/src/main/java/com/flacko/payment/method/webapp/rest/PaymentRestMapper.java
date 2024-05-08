package com.flacko.payment.method.webapp.rest;

import com.flacko.payment.method.service.PaymentMethod;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class PaymentRestMapper {

    PaymentMethodResponse mapModelToResponse(PaymentMethod paymentMethod) {
        return new PaymentMethodResponse(paymentMethod.getId(),
                paymentMethod.getType(),
                paymentMethod.getNumber(),
                paymentMethod.getHolderName(),
                paymentMethod.getCurrency(),
                paymentMethod.getBankId(),
                paymentMethod.getTraderTeamId(),
                paymentMethod.getTerminalId(),
                paymentMethod.isBusy(),
                paymentMethod.getCreatedDate().atZone(ZoneId.systemDefault()),
                paymentMethod.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
