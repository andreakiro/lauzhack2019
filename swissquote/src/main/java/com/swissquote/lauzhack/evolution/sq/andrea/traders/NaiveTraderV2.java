package com.swissquote.lauzhack.evolution.sq.andrea.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.Comparator;

public class NaiveTraderV2 extends TraderV2 {

	public NaiveTraderV2(Bank bank) {
		super(bank);
	}

	@Override
	protected void tradeWithBank(Trade trade, BigDecimal dueToClient) {
		if (wallet.getBalance(trade.base).subtract(dueToClient).signum() == -1) {
			System.out.println("Let's trade with the bank");
			// we don't have enough to pay client, we ask this amount to the bank
			Comparator magic = new Comparator(trade, true);
			
			BigDecimal dueToBank = magic.dueToMiddleMan(market.getLastRate(trade.base), market.getLastMarkup(trade.base));
			BigDecimal dueOfBank = magic.dueOfMiddleMan(market.getLastRate(trade.base), market.getLastMarkup(trade.base));
			
			bank.buy(new Trade(trade.base, trade.term, dueToClient));
			
			wallet.update(trade.base, dueOfBank);
			wallet.update(trade.term, dueToBank);
			
			printWallet();
		}
	}

	@Override
	public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
		// NOTHING CHANGES
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
