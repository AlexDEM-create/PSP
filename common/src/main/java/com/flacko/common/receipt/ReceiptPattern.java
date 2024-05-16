package com.flacko.common.receipt;

import com.flacko.common.bank.Bank;

public class ReceiptPattern {

    private static final String SBER_BANK_CARD_INTERNAL_PATTERN = """
            Чек по операции
            (?P<datetime>.+)
            (- ){36}-
            Операция
            Перевод клиенту СберБанка
            ФИО получателя
            (?P<recipient_full_name>.+)
            Номер карты получателя
            \\*\\*\\*\\* (?P<recipient_card_last_four_digits>[0-9]{4})
            ФИО отправителя
            (?P<sender_full_name>.+)
            Счёт отправителя
            \\*\\*\\*\\* (?P<sender_card_last_four_digits>[0-9]{4})
            Сумма перевода
            (?P<amount>[0-9 ]+,[0-9]{2}) (?P<amount_currency>₽)
            Комиссия
            (?P<commission>[0-9 ]+,[0-9]{2}) (?P<commission_currency>₽)
            Номер документа
            (?P<document_number>[0-9]+)
            Код авторизации
            (?P<authorization_code>[0-9]+)
            (- ){36}-
            Дополнительная информация
            Если вы отправили деньги не тому человеку,
            обратитесь к получателю перевода.
            Деньги может вернуть только получатель
            """;

    public static String getPattern(Bank bank, ReceiptPatternType type) {
        return switch (bank) {
            case SBER -> switch (type) {
                case BANK_CARD_INTERNAL -> SBER_BANK_CARD_INTERNAL_PATTERN;
                case BANK_CARD_EXTERNAL -> "Pattern for Bank A - Type 2";
                case PHONE_NUMBER_EXTERNAL -> "Pattern for Bank A - Type 3";
                default -> throw new IllegalArgumentException(
                        String.format("Unsupported receipt pattern type %s for %s", type, bank));
            };
            case RAIFFEISEN -> switch (type) {
                case BANK_CARD_INTERNAL -> "Pattern for Bank A - Type 1";
                case BANK_CARD_EXTERNAL -> "Pattern for Bank A - Type 2";
                case PHONE_NUMBER_EXTERNAL -> "Pattern for Bank A - Type 3";
                default -> throw new IllegalArgumentException(
                        String.format("Unsupported receipt pattern type %s for %s", type, bank));
            };
        };
    }

}
