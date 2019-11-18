package com.swissquote.lauzhack.evolution.sq.andrea.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Trade;

public class Comparator {
	
	private Currency from; // currency that is being bought (either by the client, or by the bank when at your request)
	@SuppressWarnings("unused")
	private Currency to; // currency that it is being exchanged from (either by the client, or by the bank when at your request)
	
	private BigDecimal amountFrom; // how much is traded, expressed in from currency
	@SuppressWarnings("unused")
	private BigDecimal amountTo;
	
	private boolean middleman; // false = client is buying, true = bank is buying

	public Comparator(Trade trade, boolean middleman) {
		this.from = trade.base;
		this.to = trade.term;
		this.amountFrom = trade.quantity;
		this.middleman = middleman;
	}
	
	public Comparator(Currency from, Currency to, BigDecimal amountFrom, boolean middleman) {
		this(new Trade(from, to, amountFrom), middleman);
	}
	
	// perte du trader always return negative value !!
	public BigDecimal dueToMiddleMan(BigDecimal lastRate, BigDecimal lastMarkup) {
		if (middleman) {
			// bank trade
			if (from.equals(Currency.CHF)) {
				return amountFrom.negate().divide(lastRate.multiply(BigDecimal.ONE.subtract(lastMarkup)), 6, RoundingMode.HALF_EVEN);
			} else {
				return amountFrom.negate().multiply(lastRate).multiply(BigDecimal.ONE.add(lastMarkup)).subtract(new BigDecimal(100));
			}
		} else {
			// client trade
			if (from.equals(Currency.CHF)) {
				return amountFrom.negate().add(BigDecimal.TEN);
			} else {
				return amountFrom.negate();
			}
		}
	}
	
	// gain du trader always return positive value !!
	public BigDecimal dueOfMiddleMan(BigDecimal lastRate, BigDecimal lastMarkup) {
		if (middleman) {
			// bank trade
			if (from.equals(Currency.CHF)) {
				return amountFrom.subtract(new BigDecimal(100));
			} else {
				return amountFrom;
			}
		} else {
			// client trade
			if (from.equals(Currency.CHF)) {
				return amountFrom.divide(lastRate.multiply(BigDecimal.ONE.subtract(lastMarkup)), 6, RoundingMode.HALF_EVEN);
			} else {
				return amountFrom.multiply(lastRate).multiply(lastMarkup).add(BigDecimal.ONE).add(BigDecimal.TEN);
			}
		}
	}
}