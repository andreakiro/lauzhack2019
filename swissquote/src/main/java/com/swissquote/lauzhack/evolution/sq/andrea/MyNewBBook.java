package com.swissquote.lauzhack.evolution.sq.andrea;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.andrea.traders.NaiveTraderV2;
import com.swissquote.lauzhack.evolution.sq.andrea.traders.TraderV2;

/**
 * This is a very simple example of implementation.
 * This class can be completely discarded.
 */
public class MyNewBBook implements BBook {

	// Save a reference to the bank in order to pass orders
	@SuppressWarnings("unused")
	private Bank bank;
	private TraderV2 trader;
	private Integer i = 0;
	
	@Override
	public void onInit() {
		trader.onInit();
	}

	@Override
	public void onTrade(Trade trade) {
//		System.out.println("Client buy " + trade.quantity + " " + trade.base + " against " + trade.term);
		trader.tradeWithClient(trade);
//		i ++;
//		if (i > 6309) {
//			onTrade2();
//			System.exit(1);
//		}
	}
	
	private void onTrade2() {
		trader.tradeWithClient(new Trade(Currency.GBP, Currency.CHF, new BigDecimal(5500)));
	}

	@Override
	public void onPrice(Price price) {
		trader.updateMarketPrices(price);
		trader.tradeWithBankWhenPricesChanges(price);
	}

	@Override
	public void setBank(Bank bank) {
		this.bank = bank;
		this.trader = new NaiveTraderV2(bank);
	}
}