package com.flacko.currency;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CurrencyParser {
    private static final String URL_STRING = "<https://garantex.org/trading/usdtrub>";
    private CurrencyService currencyService;

    public CurrencyParser(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void parseAndSaveCurrency() throws Exception {
        Document doc = Jsoup.connect(URL_STRING).get();
        String jsCode = doc.select("script").html();
        String jsonData = jsCode.substring(jsCode.indexOf("window.gon = ") + "window.gon = ".length(), jsCode.indexOf("};") + 1);
        JSONObject gon = new JSONObject(jsonData);
        JSONObject exchangers = gon.getJSONObject("exchangers");
        JSONObject usdtrub = exchangers.getJSONObject("usdtrub");
        JSONArray bid = usdtrub.getJSONArray("bid");
        JSONObject firstBidObject = bid.getJSONObject(0);
        double firstBidPrice = firstBidObject.getDouble("price");

        CurrencyBuilder currencyBuilder = currencyService.create();
        currencyBuilder.withRate(String.valueOf(firstBidPrice));
        currencyBuilder.build();
    }
}