INSERT INTO banks (id, name, country, created_date, updated_date)
VALUES ('zwkotolglnickj', 'Sberbank', 'Russia', now(), now());

INSERT INTO bank_patterns (id, pattern, type, bank_id, created_date, updated_date)
VALUES ('snikjmbfvdylwt', 'Чек по операции\n.+\n(- ){36}-\nОперация\nПеревод клиенту СберБанка\nФИО получателя\n(?P<recipientFullName>.+)\nНомер карты получателя\n\*\*\*\* (?P<recipientCardLastFourDigits>[0-9]{4})\nФИО отправителя\n(?P<senderFullName>.+)\nСчёт отправителя\n\*\*\*\* (?P<senderCardLastFourDigits>[0-9]{4})\nСумма перевода\n(?P<amount>[0-9 ]+,[0-9]{2}) (?P<amountCurrency>₽)\nКомиссия\n(?P<commission>[0-9 ]+,[0-9]{2}) (?P<commissionCurrency>₽)\nНомер документа\n(?P<documentNumber>[0-9]+)\nКод авторизации\n(?P<authorizationCode>[0-9]+)\n(- ){36}-\nДополнительная информация\nЕсли вы отправили деньги не тому человеку,\nобратитесь к получателю перевода.\nДеньги может вернуть только получатель\n', 'RECEIPT', 'zwkotolglnickj', now(), now());
