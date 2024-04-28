package com.flacko.payment.verification.sms.impl;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.id.IdGenerator;
import com.flacko.payment.service.Payment;
import com.flacko.payment.service.PaymentService;
import com.flacko.payment.verification.sms.service.SmsPaymentVerification;
import com.flacko.payment.verification.sms.service.SmsPaymentVerificationBuilder;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationInvalidCardLastFourDigitsException;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.sms.service.exception.SmsPaymentVerificationUnexpectedAmountException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class SmsPaymentVerificationBuilderImpl implements InitializableSmsPaymentVerificationBuilder {

    private static final Pattern LAST_FOUR_DIGITS_PATTERN = Pattern.compile("^\\d{4}$");

    private final SmsPaymentVerificationRepository smsPaymentVerificationRepository;
    private final PaymentService paymentService;

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
    public SmsPaymentVerification build() throws SmsPaymentVerificationMissingRequiredAttributeException,
            SmsPaymentVerificationInvalidCardLastFourDigitsException, PaymentNotFoundException,
            SmsPaymentVerificationUnexpectedAmountException {
        SmsPaymentVerificationPojo payment = pojoBuilder.build();
        validate(payment);
        smsPaymentVerificationRepository.save(payment);
        return payment;
    }

    private void validate(SmsPaymentVerificationPojo pojo)
            throws SmsPaymentVerificationMissingRequiredAttributeException,
            SmsPaymentVerificationInvalidCardLastFourDigitsException, PaymentNotFoundException,
            SmsPaymentVerificationUnexpectedAmountException {
        if (pojo.getId() == null || pojo.getId().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("id", Optional.empty());
        }
        if (pojo.getPaymentId() == null || pojo.getPaymentId().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("paymentId", Optional.of(pojo.getId()));
        }
        Payment payment = paymentService.get(pojo.getPaymentId());
        if (pojo.getRecipientCardLastFourDigits() == null || pojo.getRecipientCardLastFourDigits().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("recipientCardLastFourDigits", Optional.of(pojo.getId()));
        } else if (!LAST_FOUR_DIGITS_PATTERN.matcher(pojo.getRecipientCardLastFourDigits()).matches()) {
            throw new SmsPaymentVerificationInvalidCardLastFourDigitsException(pojo.getId(), pojo.getPaymentId(),
                    "recipient", pojo.getRecipientCardLastFourDigits());
        }
        if (pojo.getSenderFullName() == null || pojo.getSenderFullName().isEmpty()) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("senderFullName", Optional.of(pojo.getId()));
        }
        if (pojo.getAmount() == null) {
            throw new SmsPaymentVerificationMissingRequiredAttributeException("amount", Optional.of(pojo.getId()));
        } else if (payment.getAmount().compareTo(pojo.getAmount()) != 0) {
            throw new SmsPaymentVerificationUnexpectedAmountException(pojo.getId(), pojo.getPaymentId(),
                    payment.getAmount(), pojo.getAmount());
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
    }


}

