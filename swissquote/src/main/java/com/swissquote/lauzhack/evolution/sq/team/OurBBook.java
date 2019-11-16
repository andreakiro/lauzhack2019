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
	private Trader trader = new NaiveTrader(bank);
	
	@Override
	public void onInit() {
		// Start by buying some cash. Don't search for more logic here: numbers are just random..
		bank.buy(new Trade(Currency.EUR, Currency.CHF, new BigDecimal(100000)));
		bank.buy(new Trade(Currency.JPY, Currency.CHF, new BigDecimal(1000000)));
		bank.buy(new Trade(Currency.USD, Currency.CHF, new BigDecimal(100000)));
		bank.buy(new Trade(Currency.GBP, Currency.CHF, new BigDecimal(100000)));
	}

	@Override
	public void onTrade(Trade trade) {
		trader.tradeWithClient(trade);
	}

	@Override
	public void onPrice(Price price) {
		trader.updateMarketPrices(price);
		trader.tradeWithBankWhenPricesChanges(price);
	}

	@Override
	public void setBank(Bank bank) {
		this.bank = bank;
	}
}