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
		BigDecimal diffBase = tradeWithClientLoss(trade);
		BigDecimal diffTerm = tradeWithClientGain(trade);
		tradeWithBank(diffBase, diffTerm, trade.base, trade.term);
		wallet=wallet.add(trade.base, diffBase).add(trade.term, diffTerm);
	}
	
	protected abstract void tradeWithBank(BigDecimal baseChange, BigDecimal termChange, Currency base, Currency term);
	
	public abstract void tradeWithBankWhenPricesChanges(Price latestChangedPrice);
	
	public void updateMarketPrices(Price price) {
		market.updateMarketPrices(price);
	}
	
	//send buy order to bank and update wallet
	protected void buyToBank(Trade trade) {
	    
	    bank.buy(trade);
	    wallet=wallet.add(trade.base, tradeWithBankLoss(trade)).add(trade.term,tradeWithBankGain(trade));
                 
    }
	
	//Returns the loss with 
	private BigDecimal tradeWithClientLoss(Trade trade) {
		BigDecimal quantity = trade.quantity.negate();
		if (trade.base.equals(Currency.CHF))
			quantity=quantity.add(CLIENT_GAIN);
		return quantity;
	}
	
	private BigDecimal tradeWithClientGain(Trade trade) {
		if (trade.base.equals(Currency.CHF))

			return trade.quantity.divide(market.getLastRate(trade.term).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(trade.term))), 2, RoundingMode.HALF_EVEN);

		else 
			return trade.quantity.multiply(market.getLastRate(trade.base).multiply(BigDecimal.ONE.add(market.getLastMarkup(trade.base)))).add(CLIENT_GAIN);
	}
	
	protected BigDecimal tradeWithBankLoss(Trade trade) {
	    BigDecimal quantity= trade.quantity.negate();
		if (trade.base.equals(Currency.CHF))
			return quantity.multiply(market.getLastRate(trade.term).multiply(BigDecimal.ONE.add(market.getLastMarkup(trade.term)))).subtract(BANK_LOSS);
		else 
			return quantity.divide(market.getLastRate(trade.base).multiply(BigDecimal.ONE.subtract(market.getLastMarkup(trade.base))), 2, RoundingMode.HALF_EVEN);
	}
	
	protected BigDecimal tradeWithBankGain(Trade trade) {
		if (trade.base.equals(Currency.CHF))
			return trade.quantity;
		else 
			return trade.quantity.subtract(BANK_LOSS);
	}
}