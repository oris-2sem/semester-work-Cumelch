package ru.itis.zooshop.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    private static final String API_KEY = "720a697d7175439cbb268c7c50220927";
    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseCurrency = "USD";
    private static final String targetCurrency = "RUB";

    public static BigDecimal convert(BigDecimal amount) {
        String url = "https://openexchangerates.org/api/latest.json?app_id=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        Response response;
        String jsonData;

        try {
            response = client.newCall(request).execute();
            jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject rates = jsonObject.getJSONObject("rates");
            BigDecimal baseCurrencyRate = rates.getBigDecimal(baseCurrency);
            BigDecimal targetCurrencyRate = rates.getBigDecimal(targetCurrency);

            return (amount.divide(baseCurrencyRate)).multiply(targetCurrencyRate).setScale(2, RoundingMode.CEILING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
