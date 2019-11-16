package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

public final class Trader {
	
	private Bank bank;
	private Wallet wallet;
	private MarketPrices market;
	
	private static final BigDecimal CLIENT_GAIN = new BigDecimal(10);
	private static final BigDecimal BANK_LOSS = new BigDecimal(100);
	
	public Trader(Bank bank) {
		this.bank = bank;
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void doTrade(Trade trade) {
		
		// algos malin avec la banque
		
		BigDecimal loss = tradeWithClientLoss(trade.base, trade.term, trade.quantity);
		BigDecimal gain = tradeWithClientGain(trade.base, trade.term, trade.quantity);
		
		updateWallet(trade.base, loss);		
		updateWallet(trade.term, gain);
	}
	
	private void updateWallet(Currency cur, BigDecimal amount) {
		wallet.updateWallet(cur, amount);
	}
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
	
	private BigDecimal tradeWithClientLoss(Currency base, Currency term, BigDecimal amount) {
		amount = BigDecimal.ZERO.subtract(amount);
		if (base.equals(Currency.CHF))
			amount.add(CLIENT_GAIN);
		return amount;
	}
	
	private BigDecimal tradeWithClientGain(Currency base, Currency term, BigDecimal amount) {
		if (base.equals(Currency.CHF))
			return amount.divide(market.getLastRate(term).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(term))));
		else 
			return amount.multiply(market.getLastRate(base).multiply(BigDecimal.ONE.add(market.getLastMarkup(base)))).add(CLIENT_GAIN);
	}
	
	private BigDecimal tradeWithBankLoss(Currency base, Currency term, BigDecimal amount) {
		amount = BigDecimal.ZERO.subtract(amount);
		if (base.equals(Currency.CHF))
			return amount.multiply(market.getLastRate(term).multiply(BigDecimal.ONE.add(market.getLastMarkup(term)))).subtract(BANK_LOSS);
		else 
			return amount.divide(market.getLastRate(base).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(base))));
	}
	
	private BigDecimal tradeWithBankGain(Currency base, Currency term, BigDecimal amount) {
		if (base.equals(Currency.CHF))
			return amount;
		else 
			return amount.subtract(BANK_LOSS);
	}
}