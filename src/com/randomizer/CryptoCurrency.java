package com.randomizer;

/*
 * This class is a wrapper for a Cryptocurrency that is parsed from a JSON taken from coinmarketcap.com endpoint
 */
public class CryptoCurrency implements Comparable<CryptoCurrency> {

	private String id;
	private String name;
	private String symbol;
	private String rank;
	private String price_usd;
	private String price_btc;
	private String market_cap_usd;
	private String available_supply;
	private String total_supply;
	private String percent_change_1h;
	private String percent_change_24h;
	private String price_ils;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getRank() {
		return rank;
	}

	public String getPrice_usd() {
		return price_usd;
	}

	public String getPrice_btc() {
		return price_btc;
	}

	public String getMarket_cap_usd() {
		return market_cap_usd;
	}

	public String getAvailable_supply() {
		return available_supply;
	}

	public String getTotal_supply() {
		return total_supply;
	}

	public String getPercent_change_1h() {
		return percent_change_1h;
	}

	public String getPercent_change_24h() {
		return percent_change_24h;
	}

	public String getPercent_change_7d() {
		return percent_change_7d;
	}

	public String getLast_updated() {
		return last_updated;
	}

	public String getPrice_ils() {
		return price_ils;
	}

	String percent_change_7d;
	String last_updated;

	public String toString() {
		return this.name + ": " + this.price_usd;
	}

	public int compareTo(CryptoCurrency otherCryptoCurrency) {

		double thisMarketCap = Double.parseDouble(this.getMarket_cap_usd());
		double thatMarketCap = Double.parseDouble(otherCryptoCurrency.getMarket_cap_usd());

		// Ascending order
		double marketCapDiff = thatMarketCap - thisMarketCap;

		if (marketCapDiff > 0)
			return 1;
		else if (marketCapDiff < 0)
			return -1;
		else {
			return 0;
		}
	}
}
