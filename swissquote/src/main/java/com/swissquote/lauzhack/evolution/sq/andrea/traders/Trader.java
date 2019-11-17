package com.swissquote.lauzhack.evolution.sq.andrea.traders;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.MarketPrices;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.Wallet;

public abstract class Trader {
	
	protected Bank bank;
	protected Wallet wallet;
	protected MarketPrices market;
	
	public Trader(Bank bank) {
		this.bank = bank;
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void tradeWithClient(Trade trade) {
		System.out.println("Client is buying " + trade.quantity + " " + trade.base + " against " + trade.term);
		if (!trade.base.equals(Currency.CHF))
			// when cur to CHF
			System.out.println("And 1 (A) " + trade.base + " is " + market.getLastRate(trade.base) + " " + trade.term);
		else 
			// when CHF to cur
			System.out.println("And 1 (B) " + trade.base + " is " + BigDecimal.ONE.divide(market.getLastRate(trade.term), 6, RoundingMode.HALF_EVEN) + " " + trade.term);
		
		System.out.println();
		
		BigDecimal dueToClient;
		BigDecimal dueOfClient;
		
		if (trade.base.equals(Currency.CHF)) {
			System.out.println("(A)");
			// when cur to CHF
			dueToClient = trade.quantity.negate();
			dueOfClient = trade.quantity.multiply(market.getLastRate(trade.base)).multiply(BigDecimal.ONE.add(market.getLastMarkup(trade.base))).add(new BigDecimal(10));
		} else {
			System.out.println("(B)");
			// when CHF to cur
			dueToClient = trade.quantity.negate().add(new BigDecimal(10));
			dueOfClient = trade.quantity.divide(BigDecimal.ONE.divide(market.getLastRate(trade.term), 6, RoundingMode.HALF_EVEN), 6, RoundingMode.HALF_EVEN); // miss markups
		}
		
		tradeWithBank(dueToClient, trade.base, trade.term);
		
		wallet.update(trade.base, dueToClient.negate()); // what we loos
		wallet.update(trade.term, dueOfClient.negate()); // what we gain
		
		printWallet();
		
	};
	
	protected abstract void tradeWithBank(BigDecimal dueToClient, Currency base, Currency term);
	
	public abstract void tradeWithBankWhenPricesChanges(Price latestChangedPrice);
	
	public abstract void onInit();
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
	
	protected void printWallet() {
		System.out.println("Let's print trader wallet");
		for (Currency cur : Currency.values())
        	System.out.println(cur + " " + wallet.getBalance(cur));
		System.out.println();
	}
}