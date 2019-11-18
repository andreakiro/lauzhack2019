package com.swissquote.lauzhack.evolution.sq.andrea.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.Comparator;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.MarketPrices;
import com.swissquote.lauzhack.evolution.sq.andrea.utility.Wallet;

public abstract class TraderV2 {
	
	protected Bank bank;
	protected Wallet wallet;
	protected MarketPrices market;
	
	public TraderV2(Bank bank) {
		this.bank = bank;
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void tradeWithClient(Trade trade) {
		Comparator magic = new Comparator(trade, false);
		
		Currency cur = trade.base.equals(Currency.CHF) ? trade.term : trade.base;
		
		BigDecimal dueToClient = magic.dueToMiddleMan(market.getLastRate(cur), market.getLastRate(cur));
		BigDecimal dueOfClient = magic.dueOfMiddleMan(market.getLastRate(cur), market.getLastMarkup(cur));
		
		tradeWithBank(trade, dueToClient.negate());
		
		wallet.update(trade.base, dueToClient); // what we loos
		wallet.update(trade.term, dueOfClient); // what we gain
		
		printWallet();
		
	};
	
	protected abstract void tradeWithBank(Trade trade, BigDecimal dueToClient);
	
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