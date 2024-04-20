package com.flacko.payment.verification.receipt;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class TestInput {
    public static void main(String[] args) {
        // Define the string in WIN1251 encoding
        String win1251String = "Чек по операции\n.+\n(- ){36}-\nОперация\nПеревод клиенту СберБанка\nФИО получателя\n(?P<recipientFullName>.+)\nНомер карты получателя\n\\*\\*\\*\\* (?P<recipientCardLastFourDigits>[0-9]{4})\nФИО отправителя\n(?P<senderFullName>.+)\nСчёт отправителя\n\\*\\*\\*\\* (?P<senderCardLastFourDigits>[0-9]{4})\nСумма перевода\n(?P<amount>[0-9 ]+,[0-9]{2}) (?P<amountCurrency>₽)\nКомиссия\n(?P<commission>[0-9 ]+,[0-9]{2}) (?P<commissionCurrency>₽)\nНомер документа\n(?P<documentNumber>[0-9]+)\nКод авторизации\n(?P<authorizationCode>[0-9]+)\n(- ){36}-\nДополнительная информация\nЕсли вы отправили деньги не тому человеку,\nобратитесь к получателю перевода.\nДеньги может вернуть только получатель\n";

        // Convert the string to UTF-8 encoding
        try {
            byte[] utf8Bytes = win1251String.getBytes("UTF-8");
            String utf8String = new String(utf8Bytes, "UTF-8");

            // Print the UTF-8 string
            System.out.println(utf8String);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding: " + e.getMessage());
        }
    }

}
