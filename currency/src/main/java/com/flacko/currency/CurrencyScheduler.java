package com.flacko.currency;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurrencyScheduler {

    private final CurrencyParser currencyParser;

    public CurrencyScheduler(CurrencyService currencyService) {
        this.currencyParser = new CurrencyParser(currencyService);
        scheduleUpdateCurrencyRate();
    }

    private void scheduleUpdateCurrencyRate() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                currencyParser.parseAndSaveCurrency();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}


