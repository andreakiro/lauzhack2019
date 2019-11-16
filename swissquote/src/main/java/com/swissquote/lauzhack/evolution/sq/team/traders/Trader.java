package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.team.utility.MarketPrices;
import com.swissquote.lauzhack.evolution.sq.team.utility.Wallet;

public abstract class Trader {
	
	protected Bank bank;
	protected Wallet wallet;
	protected MarketPrices market;
	
	private static final BigDecimal CLIENT_GAIN = new BigDecimal(10);
	private static final BigDecimal BANK_LOSS = new BigDecimal(100);
	
	public Trader(Bank bank) {
		this.bank = bank;
		this.wallet = new Wallet();
		this.market = new MarketPrices();
	}
	
	public void tradeWithClient(Trade trade) {
		BigDecimal loss = tradeWithClientLoss(trade.base, trade.term, trade.quantity);
		BigDecimal gain = tradeWithClientGain(trade.base, trade.term, trade.quantity);
		tradeWithBank(loss, gain, trade.base, trade.term);
	};
	
	protected abstract void tradeWithBank(BigDecimal loss, BigDecimal gain, Currency base, Currency term);
	
	public abstract void tradeWithBankWhenPricesChanges(Price latestChangedPrice);
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
	
	protected void updateWallet(Currency cur, BigDecimal amount) {
		wallet.updateWallet(cur, amount);
	}
	
	private BigDecimal tradeWithClientLoss(Currency base, Currency term, BigDecimal amount) {
		amount = BigDecimal.ZERO.subtract(amount);
		if (base.equals(Currency.CHF))
			amount.add(CLIENT_GAIN);
		return amount;
	}
	
	private BigDecimal tradeWithClientGain(Currency base, Currency term, BigDecimal amount) {
		if (base.equals(Currency.CHF))
			return amount.divide(market.getLastRate(term).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(term))), 2, RoundingMode.HALF_EVEN);
		else 
			return amount.multiply(market.getLastRate(base).multiply(BigDecimal.ONE.add(market.getLastMarkup(base)))).add(CLIENT_GAIN);
	}
	
	protected BigDecimal tradeWithBankLoss(Currency base, Currency term, BigDecimal amount) {
		amount = BigDecimal.ZERO.subtract(amount);
		if (base.equals(Currency.CHF))
			return amount.multiply(market.getLastRate(term).multiply(BigDecimal.ONE.add(market.getLastMarkup(term)))).subtract(BANK_LOSS);
		else 
			return amount.divide(market.getLastRate(base).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(base))), 2, RoundingMode.HALF_EVEN);
	}
	
	protected BigDecimal tradeWithBankGain(Currency base, Currency term, BigDecimal amount) {
		if (base.equals(Currency.CHF))
			return amount;
		else 
			return amount.subtract(BANK_LOSS);
	}
}