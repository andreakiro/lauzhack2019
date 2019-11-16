package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;

public class Administrator {
	
	private Wallet wallet;
	private MarketPrices market;
	
	public Administrator() {
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void updateWallet(Currency in, BigDecimal amount, Currency out) {
		wallet.updateWallet(market, in, amount, out);
	}
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
}