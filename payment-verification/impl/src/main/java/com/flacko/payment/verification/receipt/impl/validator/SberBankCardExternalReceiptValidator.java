package com.flacko.payment.verification.receipt.impl.validator;

import com.flacko.common.bank.Bank;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class SberBankCardExternalReceiptValidator implements ReceiptValidator {

    private static final String DATETIME = "datetime";
    private static final String RECIPIENT_CARD_LAST_FOUR_DIGITS = "recipient_card_last_four_digits";
    private static final String SENDER_FULL_NAME = "sender_full_name";
    private static final String SENDER_CARD_LAST_FOUR_DIGITS = "sender_card_last_four_digits";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_CURRENCY = "amount_currency";
    private static final String RECIPIENT_BANK = "recipient_bank";

    @Override
    public void validate(OutgoingPayment outgoingPayment, PaymentMethod paymentMethod,
                         Map<String, Object> extractedData) throws ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationCurrencyNotSupportedException {
        if (extractedData.containsKey(SENDER_FULL_NAME)) {
            String extractedFullName = extractedData.get(SENDER_FULL_NAME).toString();
            String extractedFirstName = extractedFullName.substring(0, extractedFullName.indexOf(' '));
            if (!extractedFirstName.equalsIgnoreCase(paymentMethod.getFirstName())) {
                log.warn("Sender first name doesn't match for outgoing payment {}. " +
                                "Expected sender first name: {}, actual sender first name: {}", outgoingPayment
                                .getId(),
                        paymentMethod.getFirstName(), extractedFirstName);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Sender first name is missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(RECIPIENT_BANK)) {
            Bank extractedRecipientBank = parseBank(extractedData.get(RECIPIENT_BANK).toString());
            if (extractedRecipientBank != outgoingPayment.getBank()) {
                log.warn("Recipient bank doesn't match for outgoing payment {}. " +
                                "Expected recipient bank: {}, actual recipient bank: {}", outgoingPayment.getId(),
                        outgoingPayment.getBank(), extractedRecipientBank);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Recipient bank is missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(SENDER_CARD_LAST_FOUR_DIGITS)) {
            String paymentMethodLastFourDigits = paymentMethod.getNumber()
                    .substring(paymentMethod.getNumber().length() - 4);
            if (!extractedData.get(SENDER_CARD_LAST_FOUR_DIGITS).equals(paymentMethodLastFourDigits)) {
                log.warn("Sender card last 4 digits don't match for outgoing payment {}. " +
                                "Expected sender card last 4 digits: {}, actual sender card last 4 digits: {}",
                        outgoingPayment.getId(), paymentMethodLastFourDigits,
                        extractedData.get(SENDER_CARD_LAST_FOUR_DIGITS));
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Sender card last 4 digits are missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(RECIPIENT_CARD_LAST_FOUR_DIGITS)) {
            String outgoingPaymentLastFourDigits = outgoingPayment.getRecipient()
                    .substring(outgoingPayment.getRecipient().length() - 4);
            if (!extractedData.get(RECIPIENT_CARD_LAST_FOUR_DIGITS).equals(outgoingPaymentLastFourDigits)) {
                log.warn("Recipient card last 4 digits don't match for outgoing payment {}. " +
                                "Expected recipient card last 4 digits: {}, actual recipient card last 4 digits: {}",
                        outgoingPayment.getId(), outgoingPaymentLastFourDigits,
                        extractedData.get(RECIPIENT_CARD_LAST_FOUR_DIGITS));
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Recipient card last 4 digits are missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(AMOUNT)) {
            BigDecimal extractedAmount = parseBigDecimal(extractedData.get(AMOUNT).toString());
            if (extractedAmount.compareTo(outgoingPayment.getAmount()) != 0) {
                log.warn("Amount doesn't match for outgoing payment {}. Expected amount: {}, actual amount: {}",
                        outgoingPayment.getId(), outgoingPayment.getAmount(), extractedAmount);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Amount is missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(AMOUNT_CURRENCY)) {
            Currency extractedAmountCurrency = parseCurrency(extractedData.get(AMOUNT_CURRENCY).toString());
            if (extractedAmountCurrency != outgoingPayment.getCurrency()) {
                log.warn("Amount currency doesn't match for outgoing payment {}. " +
                                "Expected amount currency: {}, actual amount currency: {}",
                        outgoingPayment.getId(), outgoingPayment.getCurrency(), extractedAmountCurrency);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Amount currency is missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }

        if (extractedData.containsKey(DATETIME)) {
            Instant extractedDatetime = parseDatetime(extractedData.get(DATETIME).toString());
            if (!extractedDatetime.isAfter(outgoingPayment.getCreatedDate())) {
                log.warn("Datetime is not after outgoing payment created date for outgoing payment {}. " +
                                "Expected datetime before: {}, actual datetime: {}",
                        outgoingPayment.getId(), outgoingPayment.getCreatedDate(), extractedDatetime);
                throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
            }
        } else {
            log.warn("Datetime is missing for outgoing payment {}", outgoingPayment.getId());
            throw new ReceiptPaymentVerificationFailedException(outgoingPayment.getId());
        }
    }

    private Instant parseDatetime(String inputDatetime) {
        Map<String, String> translationMap = new HashMap<>();
        translationMap.put("мск", "+0300");

        StringBuilder translatedDatetime = new StringBuilder(inputDatetime);
        for (Map.Entry<String, String> entry : translationMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            translatedDatetime = new StringBuilder(translatedDatetime.toString().replace(key, value));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss Z", Locale.of("ru"));
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(translatedDatetime, formatter);
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toInstant();
    }

    private Bank parseBank(String inputBank) {
        Map<String, String> translationMap = new HashMap<>();
        translationMap.put("Тинькофф Банк", "TINKOFF"); // no information about the actual name
        translationMap.put("Альфа Банк", "ALFA"); // no information about the actual name
        translationMap.put("Райффайзенбанк", "RAIFFEISEN");
        translationMap.put("VTB BANK", "VTB");
        StringBuilder transliteratedBank = new StringBuilder(inputBank);
        for (Map.Entry<String, String> entry : translationMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            transliteratedBank = new StringBuilder(transliteratedBank.toString().replace(key, value));
        }
        return Bank.valueOf(transliteratedBank.toString().toUpperCase());
    }

}
