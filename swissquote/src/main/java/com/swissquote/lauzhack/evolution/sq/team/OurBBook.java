package com.swissquote.lauzhack.evolution.sq.team;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.team.traders.NaiveTrader;
import com.swissquote.lauzhack.evolution.sq.team.traders.Trader;

/**
 * This is a very simple example of implementation.
 * This class can be completely discarded.
 */
public class OurBBook implements BBook {

	// Save a reference to the bank in order to pass orders
	private Bank bank;
	private Trader trader;
	
	@Override
	public void onInit() {
	    trader.initialTrade();
	}

	@Override
	public void onTrade(Trade trade) {

		trader.clientTradesWithUs(trade);

	}

	@Override
	public void onPrice(Price price) {
		trader.updateMarketPrices(price);
		trader.tradeWithBankWhenPricesChanges(price);
	}

	@Override
	public void setBank(Bank bank) {
		this.bank = bank;
		this.trader= new NaiveTrader(bank);
		        
	}
}