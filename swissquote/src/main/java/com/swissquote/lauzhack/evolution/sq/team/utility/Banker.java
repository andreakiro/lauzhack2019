package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

public final class Banker {
	
	private Wallet wallet;
	private MarketPrices market;
	
	public Banker() {
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void doTrade(Trade trade) {
		
	}
	
	private void updateWallet(Currency cur, BigDecimal amount) {
		wallet.updateWallet(cur, amount);
	}
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
}