package com.flacko.payment.verification.receipt.impl.validator;

import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationCurrencyNotSupportedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidAmountException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class SberPhoneNumberInternalReceiptValidator implements ReceiptValidator {

    private static final String DATETIME = "datetime";
    private static final String RECIPIENT_PHONE_NUMBER = "recipient_phone_number";
    private static final String SENDER_FULL_NAME = "sender_full_name";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_CURRENCY = "amount_currency";

    @Override
    public void validate(OutgoingPayment outgoingPayment, PaymentMethod paymentMethod,
                         Map<String, Object> extractedData) throws ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationCurrencyNotSupportedException {
        if (extractedData.containsKey(SENDER_FULL_NAME)) {
            String extractedFullName = extractedData.get(SENDER_FULL_NAME).toString();
            String extractedFirstName = extractedFullName.substring(0, extractedFullName.indexOf(' '));
            if (!extractedFirstName.equals(paymentMethod.getFirstName())) {
                log.warn("Sender first name doesn't match for outgoing payment {}. " +
                                "Expected first name: {}, actual full name: {}", outgoingPayment.getId(),
                        paymentMethod.getFirstName(), extractedFirstName);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        }
        if (extractedData.containsKey(RECIPIENT_PHONE_NUMBER)) {
            String outgoingPaymentRecipient = sanitizePhoneNumber(outgoingPayment.getRecipient());
            String extractedRecipientPhoneNumber =
                    sanitizePhoneNumber(extractedData.get(RECIPIENT_PHONE_NUMBER).toString());
            if (!extractedRecipientPhoneNumber.equals(outgoingPaymentRecipient)) {
                log.warn("Recipient phone number don't match for outgoing payment {}. " +
                                "Expected phone number: {}, phone number: {}",
                        outgoingPayment.getId(), outgoingPaymentRecipient,
                        extractedRecipientPhoneNumber);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        }
        if (extractedData.containsKey(AMOUNT)) {
            BigDecimal extractedAmount = parseBigDecimal(extractedData.get(AMOUNT).toString());
            if (extractedAmount.compareTo(outgoingPayment.getAmount()) != 0) {
                log.warn("Amount doesn't match for outgoing payment {}. Expected amount: {}, actual amount: {}",
                        outgoingPayment.getId(), outgoingPayment.getAmount(), extractedAmount);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        }
        if (extractedData.containsKey(AMOUNT_CURRENCY)) {
            Currency extractedAmountCurrency = parseCurrency(extractedData.get(AMOUNT_CURRENCY).toString());
            if (extractedAmountCurrency != outgoingPayment.getCurrency()) {
                log.warn("Amount currency doesn't match for outgoing payment {}. " +
                                "Expected amount currency: {}, actual amount currency: {}",
                        outgoingPayment.getId(), outgoingPayment.getCurrency(), extractedAmountCurrency);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        }
        if (extractedData.containsKey(DATETIME)) {
            Instant extractedDatetime = parseDatetime(extractedData.get(DATETIME).toString());
            if (!extractedDatetime.isAfter(outgoingPayment.getCreatedDate())) {
                log.warn("Datetime is not after outgoing payment created date for outgoing payment {}. " +
                                "Expected datetime before: {}, actual datetime: {}",
                        outgoingPayment.getId(), outgoingPayment.getCreatedDate(), extractedDatetime);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        }
    }

    private Instant parseDatetime(String inputDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss (zzz)", Locale.of("ru"));
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputDatetime, formatter);
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toInstant();
    }

    private String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber
                .replaceAll(" ", "")
                .replaceAll("\\+", "")
                .replaceAll("-", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "");
    }

}
