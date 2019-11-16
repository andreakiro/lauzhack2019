package com.swissquote.lauzhack.evolution.sq.team;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.team.utility.Banker;

/**
 * This is a very simple example of implementation.
 * This class can be completely discarded.
 */
public class OurBBook implements BBook {

	// Save a reference to the bank in order to pass orders
	private Bank bank;
	
	private Banker admin = new Banker();
	
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
		admin.doTrade(trade);
		
//		if (Math.random() < 0.05) {
//			Trade coverTrade = new Trade(trade.base, trade.term, trade.quantity.multiply(new BigDecimal(2)));
//			bank.buy(coverTrade);
//		}
	}

	@Override
	public void onPrice(Price price) {
		admin.updateMarketPrices(price);
	}

	@Override
	public void setBank(Bank bank) {
		this.bank = bank;
	}
}