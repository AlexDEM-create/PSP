package com.flacko.payment.verification.sms;

import com.flacko.auth.id.IdGenerator;
import com.flacko.payment.verification.sms.exception.SmsPaymentVerificationMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class SmsPaymentVerificationBuilderImpl implements InitializableSmsPaymentVerificationBuilder {

    private SmsPaymentVerificationPojo.SmsPaymentVerificationPojoBuilder pojoBuilder;

    @Override
    public SmsPaymentVerificationBuilder initializeNew() {
        pojoBuilder = SmsPaymentVerificationPojo.builder()
                .id(new IdGenerator().generateId());
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withPaymentId(String paymentId) {
        pojoBuilder.paymentId(paymentId);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withRecipientCardLastFourDigits(String recipientCardLastFourDigits) {
        pojoBuilder.recipientCardLastFourDigits(recipientCardLastFourDigits);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withSenderFullName(String senderFullName) {
        pojoBuilder.senderFullName(senderFullName);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withAmount(BigDecimal amount) {
        pojoBuilder.amount(amount);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withAmountCurrency(Currency amountCurrency) {
        pojoBuilder.amountCurrency(amountCurrency);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withMessage(String message) {
        pojoBuilder.message(message);
        return this;
    }

    @Override
    public SmsPaymentVerificationBuilder withData(Map<String, Object> data) {
        pojoBuilder.data(Collections.unmodifiableMap(data));
        return this;
    }

    @Override
    public SmsPaymentVerification build() throws SmsPaymentVerificationMissingRequiredAttributeException {
        SmsPaymentVerificationPojo payment = pojoBuilder.build();
        validate(payment);
        return payment;
    }

    private void validate(SmsPaymentVerificationPojo pojo) throws SmsPaymentVerificationMissingRequiredAttributeException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        // validate card number
        if (pojo.getRecipientCardLastFourDigits() == null || pojo.getRecipientCardLastFourDigits().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("recipientCardLastFourDigits", Optional.of(pojo.getId()));
        }
        if (pojo.getSenderFullName() == null || pojo.getSenderFullName().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("senderFullName", Optional.of(pojo.getId()));
        }
        if (pojo.getAmount() == null) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        }
        // validate currency is supported
        if (pojo.getAmountCurrency() == null) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("amountCurrency", Optional.of(pojo.getId()));
        }
        // what to do with not payment related sms?
        if (pojo.getMessage() == null || pojo.getMessage().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("message", Optional.of(pojo.getId()));
        }
        if (pojo.getData() == null || pojo.getData().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("data", Optional.of(pojo.getId()));
        }
        if (pojo.getCreatedDate() == null) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("createdDate", Optional.of(pojo.getId()));
        }
    }


}

