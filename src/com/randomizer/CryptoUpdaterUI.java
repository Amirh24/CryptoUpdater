package com.randomizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CryptoUpdaterUI {

	private BufferedReader br;
	private CryptoUpdater cryptoUpdater;

	public BufferedReader getBr() {
		return br;
	}

	public CryptoUpdater getCryptoUpdater() {
		return cryptoUpdater;
	}

	public CryptoUpdaterUI() {

		br = new BufferedReader(new InputStreamReader(System.in));
		cryptoUpdater = new CryptoUpdater();
	}

	public static void main(String[] args) throws Exception {

		CryptoUpdaterUI cryptoUpdaterUI = new CryptoUpdaterUI();

		cryptoUpdaterUI.start();

	}

	public void start() {
		System.out.println("Welcome to CryptoUpdater, here are the following instructions: ");
		displayInstructions();
		while (true) {
			String input = null;
			try {
				input = br.readLine().toLowerCase();
			} catch (IOException e) {
				System.out.println("Please choose one of the avialable options, for help type \"-h\"");
			}
			switch (input) {
			case "add":
				subscribeToACoin();
				break;
			case "remove":
				removeSubscriptionFromCoin();
				break;
			case "show":
				showSubscriptionsDetails();
				break;
			case "view":
				viewGeneralDetails();
				break;
			case "-h":
				displayInstructions();
				break;
			default:
				System.out.println("Please choose one of the avialable options, for help type \"-h\"");
			}
		}
	}

	private void displayInstructions() {
		System.out.println("type \"Add\" in order to subscribe to the currency");
		System.out.println("type \"Remove\" in order to remove a currency from the subscriptions");
		System.out.println("type \"Show\" in order to see the currencies your are subscribed to");
		System.out.println("type \"View\" in order to get details on an amount of cryptoCurrencies of your choise");
		System.out.println("type \"-h\" whenever you wish to see the instructions again.");
	}

	public void subscribeToACoin() {

		// If data was never fetched from DB, get it
		if (cryptoUpdater.getCoinsSubscriptions().isEmpty()) {
			cryptoUpdater.getSubscriptionsFromDB();
		}

		String coinSymbol = null;
		CryptoCurrency coinToSubscribe = null;

		while (coinToSubscribe == null) {
			try {
				System.out.println("Please write coin symbol name in order to subscribe to a coin");
				coinSymbol = br.readLine();
				coinToSubscribe = cryptoUpdater.subscribeToACoin(coinSymbol.toUpperCase());
			}

			catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public void removeSubscriptionFromCoin() {

		// If data was never fetched from DB, get it
		if (cryptoUpdater.getCoinsSubscriptions().isEmpty()) {
			cryptoUpdater.getSubscriptionsFromDB();
		}

		String coinSymbol = null;
		CryptoCurrency coinToSubscribe = null;

		while (coinToSubscribe == null) {
			try {
				System.out.println("Please write coin symbol name in order to remove it from subscriptions");
				coinSymbol = br.readLine();
				coinToSubscribe = cryptoUpdater.removeSubscriptionFromCoin(coinSymbol.toUpperCase());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public void showSubscriptionsDetails() {
		// If data was never fetched from DB, get it
		if (cryptoUpdater.getCoinsSubscriptions().isEmpty()) {
			cryptoUpdater.getSubscriptionsFromDB();
		}

		for (CryptoCurrency subscribedCryptoCurrency : cryptoUpdater.getCoinsSubscriptions()) {
			System.out.println(subscribedCryptoCurrency.getName() + ", " + subscribedCryptoCurrency.getSymbol() + ": "
					+ subscribedCryptoCurrency.getPrice_ils());
		}
	}

	public void viewGeneralDetails() {

		int numberOfCoins = -1;
		try {
			System.out.println("Please choose the number of coins you wish to get details on");
			System.out.println("The number of coins should be at the range of 1 - 1110");

			numberOfCoins = Integer.parseInt(br.readLine());
			System.err.println(cryptoUpdater.getCoinMarketCap().getMinimalDataInILS(numberOfCoins));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
