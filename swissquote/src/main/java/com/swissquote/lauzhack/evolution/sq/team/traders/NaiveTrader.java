package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

public class NaiveTrader extends Trader {

	public NaiveTrader(Bank bank) {
		super(bank);
	}

	@Override
	protected void tradeWithBank(BigDecimal loss, BigDecimal gain, Currency base, Currency term) {
		if (wallet.getBalance(base).compareTo(loss) == -1) {
			while (wallet.getBalance(base).compareTo(loss) != 1) {
				bank.buy(new Trade(Currency.EUR, Currency.CHF, new BigDecimal(100000)));
			}
		}
	}

	@Override
	public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {}
}