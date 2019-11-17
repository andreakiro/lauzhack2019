package com.swissquote.lauzhack.evolution.sq.andrea;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.andrea.traders.NaiveTrader;
import com.swissquote.lauzhack.evolution.sq.andrea.traders.Trader;

/**
 * This is a very simple example of implementation.
 * This class can be completely discarded.
 */
public class MyNewBBook implements BBook {

	// Save a reference to the bank in order to pass orders
	@SuppressWarnings("unused")
	private Bank bank;
	private Trader trader;
	
	@Override
	public void onInit() {
		trader.onInit();
	}

	@Override
	public void onTrade(Trade trade) {
		System.out.println("Let's trade with a new client");
		trader.tradeWithClient(new Trade(Currency.USD, Currency.CHF, new BigDecimal(100)));
		trader.tradeWithClient(new Trade(Currency.CHF, Currency.JPY, new BigDecimal(100)));
		System.exit(1);
	}

	@Override
	public void onPrice(Price price) {
		trader.updateMarketPrices(price);
		trader.tradeWithBankWhenPricesChanges(price);
	}

	@Override
	public void setBank(Bank bank) {
		this.bank = bank;
		this.trader = new NaiveTrader(bank);
	}
}