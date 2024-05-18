package com.flacko.payment.verification.receipt.impl.validator;

import com.flacko.common.currency.Currency;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationCurrencyNotSupportedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidAmountException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Map;

public interface ReceiptValidator {

    void validate(OutgoingPayment outgoingPayment, PaymentMethod paymentMethod, Map<String, Object> extractedData)
            throws ReceiptPaymentVerificationFailedException, ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationCurrencyNotSupportedException;

    default BigDecimal parseBigDecimal(String amount) throws ReceiptPaymentVerificationInvalidAmountException {
        String cleanedAmountString = amount.replace(" ", "").replace(",", ".");

        try {
            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            decimalFormat.setDecimalFormatSymbols(symbols);
            return new BigDecimal(decimalFormat.parse(cleanedAmountString).toString());
        } catch (ParseException e) {
            throw new ReceiptPaymentVerificationInvalidAmountException(amount);
        }
    }

    default Currency parseCurrency(String currency) throws ReceiptPaymentVerificationCurrencyNotSupportedException {
        if ("R".equals(currency)) {
            return Currency.RUB;
        }
        throw new ReceiptPaymentVerificationCurrencyNotSupportedException(currency);
    }

}
