package com.randomizer;

import java.util.ArrayList;
import java.util.Collections;

public class CryptoUpdater {

	private MySQLDB database;
	private CoinMarketCap coinMarketCap;
	private ArrayList<CryptoCurrency> coinsSubscriptions;

	public MySQLDB getDatabase() {
		return database;
	}

	public CoinMarketCap getCoinMarketCap() {
		return coinMarketCap;
	}

	public ArrayList<CryptoCurrency> getCoinsSubscriptions() {
		return coinsSubscriptions;
	}

	public CryptoUpdater() {
		coinMarketCap = new CoinMarketCap();
		coinsSubscriptions = new ArrayList<CryptoCurrency>();
	}

	// Creates a table at first addition
	// If the coin is found, return it. Else, return null
	public CryptoCurrency subscribeToACoin(String symbolName) throws NumberFormatException, Exception {

		getDBConnectionAndTable();

		// check if you are already subscribed to the coin requested

		for (CryptoCurrency subscribedCryptoCurrency : coinsSubscriptions) {
			if (symbolName.equals(subscribedCryptoCurrency.getSymbol())) {
				System.out.println("You are already subscribed to " + subscribedCryptoCurrency.getName());
				return subscribedCryptoCurrency;
			}
		}
		// Look for a coin in the cryptocurrency array from Coin Market Cap if your
		// aren't already subscribed

		for (CryptoCurrency cryptocurrency : coinMarketCap.getCryptoCurrencies()) {
			if (symbolName.equals(cryptocurrency.getSymbol())) {
				// if found, add it to database and list
				coinsSubscriptions.add(cryptocurrency);
				database.post(cryptocurrency.getName(), cryptocurrency.getSymbol(),
						Double.parseDouble(cryptocurrency.getPrice_ils()),
						Double.parseDouble(cryptocurrency.getMarket_cap_usd()));

				Collections.sort(coinsSubscriptions);
				return cryptocurrency;
			}
		}
		return null;
	}

	// Creates a table at first removal
	// If the coin is found, return it. Else, return null
	public CryptoCurrency removeSubscriptionFromCoin(String symbolName) throws NumberFormatException, Exception {

		getDBConnectionAndTable();

		// check if you are already subscribed to the coin requested

		for (CryptoCurrency subscribedCryptoCurrency : coinsSubscriptions) {
			if (symbolName.equals(subscribedCryptoCurrency.getSymbol())) {
				coinsSubscriptions.remove(subscribedCryptoCurrency);
				database.delete(symbolName);
				System.out.println("Removed " + subscribedCryptoCurrency.getName() + " from your subscriptions");
				return subscribedCryptoCurrency;
			}
		}

		System.out.println(symbolName + " was not found in your subscriptions");
		return null;
	}

	public ArrayList<CryptoCurrency> getSubscriptionsFromDB() {

		getDBConnectionAndTable();

		// get subscriptions information from DB
		ArrayList<String> symbolNamesOfSubscriptions = new ArrayList<String>();
		try {
			symbolNamesOfSubscriptions = database.get();
		} catch (Exception e) {
			System.out.println(e);
		}

		// Add subscribed cryptoCurrncies to list
		for (String symbolName : symbolNamesOfSubscriptions) {
			for (CryptoCurrency cryptocurrency : coinMarketCap.getCryptoCurrencies()) {
				if (symbolName.equals(cryptocurrency.getSymbol())) {
					coinsSubscriptions.add(cryptocurrency);
					break;
				}
			}
		}
		return coinsSubscriptions;
	}

	private void getDBConnectionAndTable() {
		// connect To Database and create table if not already created
		try {
			database = MySQLDB.getConnection();
			database.createTable("cryptocurrencies");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
