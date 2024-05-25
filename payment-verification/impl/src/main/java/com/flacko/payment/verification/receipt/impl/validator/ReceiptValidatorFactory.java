package com.flacko.payment.verification.receipt.impl.validator;

import com.flacko.common.bank.Bank;
import com.flacko.common.receipt.ReceiptPatternType;

public class ReceiptValidatorFactory {

    public static ReceiptValidator createValidator(Bank bank, ReceiptPatternType type) {
        return switch (bank) {
            case SBER -> switch (type) {
                case BANK_CARD_INTERNAL -> new SberBankCardInternalReceiptValidator();
                case BANK_CARD_EXTERNAL -> new SberBankCardExternalReceiptValidator();
                case PHONE_NUMBER_INTERNAL -> new SberPhoneNumberInternalReceiptValidator();
                case PHONE_NUMBER_EXTERNAL -> new SberPhoneNumberExternalReceiptValidator();
            };
            case RAIFFEISEN -> switch (type) {
                case BANK_CARD_INTERNAL -> new RaiffeisenBankCardInternalReceiptValidator();
                case BANK_CARD_EXTERNAL -> new RaiffeisenBankCardExternalReceiptValidator();
                case PHONE_NUMBER_INTERNAL -> new RaiffeisenPhoneNumberInternalReceiptValidator();
                case PHONE_NUMBER_EXTERNAL -> new RaiffeisenPhoneNumberExternalReceiptValidator();
            };
            default -> throw new IllegalArgumentException("Unsupported bank " + bank);
        };
    }

}
