INSERT INTO banks (id, name, country) VALUES ('zwkotolglnickj', 'Sberbank', 'Russia');

INSERT INTO bank_patterns (id, pattern, type, bank_id) VALUES ('snikjmbfvdylwt', 'Чек по операции\n.+\n(- ){36}-\nОперация\nПеревод клиенту СберБанка\nФИО получателя\n.+\nНомер карты получателя\n**** [0-9]{4}\nФИО отправителя\n.+\nСчёт отправителя\n**** [0-9]{4}\nСумма перевода\n([0-9 ]+,[0-9]{2}) ₽\nКомиссия\n([0-9 ]+,[0-9]{2}) ₽\nНомер документа\n[0-9]+\nКод авторизации\n[0-9]+\n(- ){36}-\nДополнительная информация\nЕсли вы отправили деньги не тому человеку,\nобратитесь к получателю перевода.\nДеньги может вернуть только получатель\n', 'RECEIPT', 'zwkotolglnickj');
