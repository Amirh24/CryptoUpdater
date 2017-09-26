package com.randomizer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoinMarketCap {

	private CryptoCurrency[] cryptoCurrencies;

	public CryptoCurrency[] getCryptoCurrencies() {
		return cryptoCurrencies;
	}

	public CoinMarketCap() {

		this.cryptoCurrencies = setCryptoCurrencies();
	}

	public CryptoCurrency[] setCryptoCurrencies() {
		String JSON = consumeCoinMarketCapHebrewCryptoCurrencyAPI();
		CryptoCurrency[] cryptoCurrencies = parseJSON(JSON);

		return cryptoCurrencies;

	}

	public String getMinimalDataInILS(int numberOfCoinsToShow) {

		// make Sure that numberOfCoinsToShow is at the range of 1 - ~1110 (at the time
		// of writing)
		numberOfCoinsToShow = numberOfCoinsToShow <= 0 ? 1 : Math.min(numberOfCoinsToShow, cryptoCurrencies.length);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < numberOfCoinsToShow; i++) {
			sb.append((i + 1) + ") " + cryptoCurrencies[i].getName() + ", " + cryptoCurrencies[i].getSymbol() + ": "
					+ cryptoCurrencies[i].getPrice_ils() + " ILS\n");

		}

		return sb.toString();

	}

	public String consumeCoinMarketCapHebrewCryptoCurrencyAPI() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://api.coinmarketcap.com/v1/ticker/?convert=ILS");
		String coinMarketCapJSON = target.request(MediaType.TEXT_XML).get(String.class);

		return coinMarketCapJSON;

	}

	public CryptoCurrency[] parseJSON(String JSON) {

		Gson gson = new GsonBuilder().create();
		CryptoCurrency[] cryptoCurrencies = gson.fromJson(JSON, CryptoCurrency[].class);

		return cryptoCurrencies;

	}
}
