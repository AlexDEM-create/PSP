package com.flacko.currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://garantex.org/trading/usdtrub").get();

            String jsCode = doc.select("script").html();

            String jsonData = jsCode.substring(jsCode.indexOf("window.gon = ") + "window.gon = ".length());
            jsonData = jsonData.substring(0, jsonData.indexOf("};") + 1);

            JSONObject gon = new JSONObject(jsonData);

            JSONObject exchangers = gon.getJSONObject("exchangers");
            JSONObject usdtrub = exchangers.getJSONObject("usdtrub");
            JSONArray bid = usdtrub.getJSONArray("bid");
            JSONObject firstBidObject = bid.getJSONObject(0);
            double firstBidPrice = firstBidObject.getDouble("price");

            System.out.println("Exchangers: " + exchangers);
            System.out.println("USD to RUB: " + usdtrub);
            System.out.println("Bid: " + bid);
            System.out.println("Price from first element of bid array: " + firstBidPrice);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
