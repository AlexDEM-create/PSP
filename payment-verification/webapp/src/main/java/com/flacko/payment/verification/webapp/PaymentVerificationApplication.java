package com.flacko.payment.verification.webapp;

import com.flacko.security.AuthorizationConfig;
import com.flacko.security.SecurityConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@Import({AuthorizationConfig.class, SecurityConfig.class})
@EnableJpaRepositories(basePackages = {"com.flacko"})
@EntityScan(basePackages = {"com.flacko"})
@ComponentScan(basePackages = {"com.flacko"})
public class PaymentVerificationApplication {

    // curl -X POST -F "file=@C:\Dima\flacko\receipts\sber_receipt_1.pdf" -F "payment_id=123" http://localhost:8080/payment-verifications/receipts

    // curl -X POST -F "file=@C:\Dima\flacko\receipts\sber_receipt_1.pdf" -F "patterns=Чек по операции\n.+\n(- ){36}-\nОперация\nПеревод клиенту СберБанка\nФИО получателя\n(?P<recipient_full_name>.+)\nНомер карты получателя\n\*\*\*\* (?P<recipient_card_last_four_digits>[0-9]{4})\nФИО отправителя\n(?P<sender_full_name>.+)\nСчёт отправителя\n\*\*\*\* (?P<sender_card_last_four_digits>[0-9]{4})\nСумма перевода\n(?P<amount>[0-9 ]+,[0-9]{2}) ₽\nКомиссия\n(?P<commission>[0-9 ]+,[0-9]{2}) ₽\nНомер документа\n(?P<document_number>[ 0-9]+)\nКод авторизации\n(?P<authorization_code>[0-9]+)\n(- ){36}-\nДополнительная информация\nЕсли вы отправили деньги не тому человеку,\nобратитесь к получателю перевода.\nДеньги может вернуть только получатель\n" http://localhost:5000/payment-verifications/receipts/extract-data

    public static void main(String[] args) {
        SpringApplication.run(PaymentVerificationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }

}
