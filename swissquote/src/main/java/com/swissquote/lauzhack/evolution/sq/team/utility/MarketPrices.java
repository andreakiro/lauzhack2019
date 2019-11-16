package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;

public final class MarketPrices {
	
	// The last rate or markup is at the beginning of the list
	
	private Map<Currency, List<BigDecimal>> rates;
	private Map<Currency, List<BigDecimal>> markups;

	public MarketPrices() {
		this.rates = new HashMap<>();
		this.markups = new HashMap<>();
		
		for (Currency cur: Currency.values())
			rates.put(cur, new LinkedList<BigDecimal>());
		
		for (Currency cur: Currency.values())
			markups.put(cur, new LinkedList<BigDecimal>());
	}
	
	public BigDecimal getLastRate(Currency cur) {
		return rates.get(cur).get(0);
	}
	
	public BigDecimal getLastMarkup(Currency cur) {
		return markups.get(cur).get(0);
	}
	
	public List<BigDecimal> getRates(Currency cur) {
		return rates.get(cur);
	}
	
	public List<BigDecimal> getMarkups(Currency cur) {
		return markups.get(cur);
	}
	
	public void updateMarketPrices(Price price) {
		Currency cur = price.base;
		addLastRate(cur, price.rate);
		addLastMarkup(cur, price.markup);
	}
	
	private void addLastRate(Currency cur, BigDecimal value) {
		rates.get(cur).add(0, value);
	}
	
	private void addLastMarkup(Currency cur, BigDecimal value) {
		rates.get(cur).add(0, value);
	}
}