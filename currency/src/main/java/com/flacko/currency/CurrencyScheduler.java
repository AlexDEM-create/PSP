package com.flacko.currency;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyScheduler {

    private final CurrencyParser currencyParser;

    public CurrencyScheduler(CurrencyParser currencyParser) {
        this.currencyParser = currencyParser;
    }

    @Scheduled(fixedRate = 3600000) // Запуск каждый час
    public void updateCurrencyRate() {
        try {
            currencyParser.parseAndSaveCurrency(); // Обновление курса валюты и сохранение в базу данных
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

