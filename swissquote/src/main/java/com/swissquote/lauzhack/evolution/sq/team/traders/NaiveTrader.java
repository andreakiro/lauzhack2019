package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;

public class NaiveTrader extends Trader {

	public NaiveTrader(Bank bank) {
		super(bank);
	}

	@Override
	protected void tradeWithBank(BigDecimal loss, BigDecimal gain, Currency base, Currency term) {
		// TODO Auto-generated method stub
	}

	@Override
	public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
		// TODO Auto-generated method stub
	}
}

//if (Math.random() < 0.05) {
//Trade coverTrade = new Trade(trade.base, trade.term, trade.quantity.multiply(new BigDecimal(2)));
//bank.buy(coverTrade);
//}