package com.flacko.common.receipt;

import com.flacko.common.bank.Bank;

public class ReceiptPattern {

    private static final String SBER_BANK_CARD_INTERNAL_PATTERN = """
            ^Чек по операции
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
            \\*\\*\\*\\* (?P<sender_account_last_four_digits>[0-9]{4})
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
            Деньги может вернуть только получатель$""";

    private static final String SBER_PHONE_NUMBER_INTERNAL_PATTERN = """
            ^Чек по операции
            (?P<datetime>.+)
            (- ){36}-
            Операция
            Перевод клиенту СберБанка
            ФИО получателя
            (?P<recipient_full_name>.+)
            Телефон получателя
            (?P<recipient_phone_number>\\+7\\([0-9]{3}\\) [0-9]{3}-[0-9]{2}-[0-9]{2})
            Номер счёта получателя
            \\*\\*\\*\\* (?P<recipient_account>[0-9]{4})
            ФИО отправителя
            (?P<sender_full_name>.+)
            Счёт отправителя
            \\*\\*\\*\\* (?P<sender_account_last_four_digits>[0-9]{4})
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
            Деньги может вернуть только получатель$""";

    private static final String SBER_PHONE_NUMBER_EXTERNAL_PATTERN = """
            ^Операция
            Сумма перевода
            (?P<amount>[0-9 ]+.[0-9]{2})  (?P<amount_currency>₽)
            Комиссия
            (?P<commission>[0-9]+.[0-9]{2})  (?P<commission_currency>₽)Карта отправителя
            •••• (?P<sender_card_last_four_digits>[0-9]{4})Банк получателя
            (?P<recipient_bank>.+)ФИО получателя перевода
            (?P<recipient_full_name>.+)
            Номер операции в СБП
            (?P<operation_number>[A-Z0-9]+)
             Номер телефона получателя
            (?P<recipient_phone_number>\\+7 [0-9]{3} [0-9]{3}-[0-9]{2}-[0-9]{2})
            (?P<sender_full_name>.+)ФИО отправителяПеревод по СБП
            \s
            Чек по операции
            (?P<datetime>.+)
            (- ){36}-$""";

    private static final String SBER_BANK_CARD_EXTERNAL_PATTERN = """
            ^(?P<datetime>.+)
            (- ){36}-Перевод по номеру карты в другой банк
            (?P<sender_full_name>.+)ОТ КОГО
            \\*\\*\\*\\* (?P<sender_card_last_four_digits>[0-9]{4})ОТКУДА(?P<amount>[0-9 ]+) (?P<amount_currency>₽)СКОЛЬКО
            КОМИССИЯ
            (?P<commission>[0-9 ]+) (?P<commission_currency>₽)
            СПИСАНО
            (?P<total_amount>[0-9 ]+) (?P<total_amount_currency>₽)\\*\\*\\*\\* (?P<recipient_card_last_four_digits>[0-9]{4})КУДА
            СТРАНА
            РОССИЯВ БАНК
            (?P<recipient_bank>.+)$""";

    public static String getPattern(Bank bank, ReceiptPatternType type) {
        return switch (bank) {
            case SBER -> switch (type) {
                case BANK_CARD_INTERNAL -> SBER_BANK_CARD_INTERNAL_PATTERN;
                case BANK_CARD_EXTERNAL -> SBER_BANK_CARD_EXTERNAL_PATTERN;
                case PHONE_NUMBER_INTERNAL -> SBER_PHONE_NUMBER_INTERNAL_PATTERN;
                case PHONE_NUMBER_EXTERNAL -> SBER_PHONE_NUMBER_EXTERNAL_PATTERN;
            };
            case RAIFFEISEN -> switch (type) {
                case BANK_CARD_INTERNAL -> "Pattern for Bank A - Type 1";
                case BANK_CARD_EXTERNAL -> "Pattern for Bank A - Type 2";
                case PHONE_NUMBER_INTERNAL -> "Pattern for Bank A - Type 3";
                case PHONE_NUMBER_EXTERNAL -> "Pattern for Bank A - Type 4";
            };
            default -> throw new IllegalArgumentException("Unsupported bank " + bank);
        };
    }

}
