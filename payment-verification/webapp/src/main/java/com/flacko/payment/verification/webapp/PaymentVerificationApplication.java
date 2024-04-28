package com.flacko.payment.verification.webapp;

import com.flacko.security.AuthorizationConfig;
import com.flacko.security.SecurityConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@Import({AuthorizationConfig.class, SecurityConfig.class})
@EnableJpaRepositories(basePackages = {"com.flacko"})
@EntityScan(basePackages = {"com.flacko"})
@ComponentScan(basePackages = {"com.flacko"})
public class PaymentVerificationApplication {

    // curl -X POST -F "file=@C:\Dima\flacko\receipts\sber_receipt_example.pdf" -F "payment_id=123" http://localhost:8080/payment-verification/receipt

    // curl -X POST -F "file=@C:\Dima\flacko\receipts\sber_receipt_example.pdf" -F "patterns=Чек по операции\n.+\n(- ){36}-\nОперация\nПеревод клиенту СберБанка\nФИО получателя\n(?P<recipient_full_name>.+)\nНомер карты получателя\n\*\*\*\* (?P<recipient_card_last_four_digits>[0-9]{4})\nФИО отправителя\n(?P<sender_full_name>.+)\nСчёт отправителя\n\*\*\*\* (?P<sender_card_last_four_digits>[0-9]{4})\nСумма перевода\n(?P<amount>[0-9 ]+,[0-9]{2}) ₽\nКомиссия\n(?P<commission>[0-9 ]+,[0-9]{2}) ₽\nНомер документа\n(?P<document_number>[ 0-9]+)\nКод авторизации\n(?P<authorization_code>[0-9]+)\n(- ){36}-\nДополнительная информация\nЕсли вы отправили деньги не тому человеку,\nобратитесь к получателю перевода.\nДеньги может вернуть только получатель\n" http://localhost:5000/payment-verification/receipt/extract-data

    public static void main(String[] args) {
        SpringApplication.run(PaymentVerificationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

}
