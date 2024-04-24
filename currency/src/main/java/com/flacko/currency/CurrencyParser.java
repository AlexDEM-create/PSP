package com.flacko.currency;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.flacko.currency.CurrencyBuilder;
import com.flacko.currency.CurrencyService;

public class CurrencyParser {

    private static final String URL_STRING = "<https://garantex.org/trading/usdtrub>";
    private static final String REGEX = "your regex here";
    private CurrencyService currencyService;

    public CurrencyParser(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void parseAndSaveCurrency() throws Exception {
        String html = getHTML(URL_STRING);
        String neededNumberString = getNeededNumber(html);
        double neededNumber = Double.parseDouble(neededNumberString);

        CurrencyBuilder currencyBuilder = currencyService.create();
        currencyBuilder.withRate(String.valueOf(neededNumber));
        currencyBuilder.build();
    }

    private String getHTML(String urlString) throws Exception {
        StringBuilder html = new StringBuilder();

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            html.append(inputLine);
        }
        in.close();

        return html.toString();
    }

    private String getNeededNumber(String html) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("No match found for regex: " + REGEX);
        }
    }
}
