package com.flacko.currency.webapp;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.CurrencyExchangeNotFoundException;
import com.flacko.currency.service.CurrencyExchange;
import com.flacko.currency.service.CurrencyExchangeBuilder;
import com.flacko.currency.service.CurrencyExchangeService;
import com.flacko.currency.service.exception.CurrencyExchangeInvalidExchangeRateException;
import com.flacko.currency.service.exception.CurrencyExchangeMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeScheduler {

    private static final String GARANTEX_URL = "https://garantex.org/trading/usdtrub";

    private final CurrencyExchangeService currencyExchangeService;

    @Scheduled(fixedRate = 1000)
    public void updateUsdtRubExchangeRate() {
        try {
            Document doc = Jsoup.connect(GARANTEX_URL).get();
            String jsCode = doc.select("script").html();
            String jsonData = jsCode.substring(jsCode.indexOf("window.gon = ") + "window.gon = ".length());
            jsonData = jsonData.substring(0, jsonData.indexOf("};") + 1);
            JSONObject gon = new JSONObject(jsonData);
            JSONObject exchangers = gon.getJSONObject("exchangers");
            JSONObject usdtrub = exchangers.getJSONObject("usdtrub");

            JSONArray bid = usdtrub.getJSONArray("bid");
            JSONObject firstBidObject = bid.getJSONObject(0);
            BigDecimal firstBidPrice = firstBidObject.getBigDecimal("price");

            JSONArray ask = usdtrub.getJSONArray("ask");
            JSONObject firstAskObject = ask.getJSONObject(0);
            BigDecimal firstAskPrice = firstAskObject.getBigDecimal("price");

            try {
                CurrencyExchange existingCurrencyExchange = currencyExchangeService.get(Currency.USDT, Currency.RUB);
                CurrencyExchangeBuilder currencyExchangeBuilder = currencyExchangeService.update(
                        existingCurrencyExchange.getSourceCurrency(), existingCurrencyExchange.getTargetCurrency());
                currencyExchangeBuilder.withBuyExchangeRate(firstBidPrice)
                        .withSellExchangeRate(firstAskPrice)
                        .build();
            } catch (CurrencyExchangeNotFoundException e) {
                currencyExchangeService.create()
                        .withSourceCurrency(Currency.USDT)
                        .withTargetCurrency(Currency.RUB)
                        .withBuyExchangeRate(firstBidPrice)
                        .withSellExchangeRate(firstAskPrice)
                        .build();
            }
        } catch (CurrencyExchangeMissingRequiredAttributeException | CurrencyExchangeInvalidExchangeRateException |
                IOException e) {
            log.error(String.format("Could not update currency exchange for %s-%s", Currency.USDT, Currency.RUB), e);
        }
    }

}
