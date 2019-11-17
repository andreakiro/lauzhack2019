package com.swissquote.lauzhack.evolution.sq.andrea.traders;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;

public class NaiveTrader extends Trader {

	public NaiveTrader(Bank bank) {
		super(bank);
	}

	@Override
	protected void tradeWithBank(BigDecimal dueToClient, Currency base, Currency term) {
		System.out.println("Let's trade something with the bank");
		if (wallet.getBalance(base).subtract(dueToClient).signum() == -1) { 
			BigDecimal traderDueToBank;
			if (base.equals(Currency.CHF)) {
				// when cur to CHF
				System.out.println("Bank (A)");
				traderDueToBank = dueToClient.negate().multiply(market.getLastRate(base)).multiply(market.getLastMarkup(base).add(BigDecimal.ONE)).subtract(new BigDecimal(100));
			} else {
				// when CHF to cur
				System.out.println("Bank (B)");
				traderDueToBank = dueToClient.negate().divide(BigDecimal.ONE.divide(market.getLastRate(term), 6, RoundingMode.HALF_EVEN), 6, RoundingMode.HALF_EVEN); // miss markups
			}
			wallet.update(base, dueToClient); // what we gain
			wallet.update(term, traderDueToBank); // what we loose
		}
		printWallet();
	}

	@Override
	public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit() {
        market.updateMarketPrices(new Price(Currency.CHF, Currency.CHF, BigDecimal.ONE, BigDecimal.ZERO));
        market.updateMarketPrices(new Price(Currency.EUR, Currency.CHF, new BigDecimal(1.09), new BigDecimal(0.001)));
        market.updateMarketPrices(new Price(Currency.USD, Currency.CHF, new BigDecimal(0.99), new BigDecimal(0.001)));
        market.updateMarketPrices(new Price(Currency.GBP, Currency.CHF, new BigDecimal(1.27), new BigDecimal(0.005)));
        market.updateMarketPrices(new Price(Currency.JPY, Currency.CHF, new BigDecimal(0.0091), new BigDecimal(0.003)));
        
        printWallet();
	}

}