package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.swissquote.lauzhack.evolution.api.Currency;

public final class Wallet {
	
	private Map<Currency, BigDecimal> wallet;
	
	public Wallet() {
		this.wallet = new HashMap<>();
		
		for (Currency cur: Currency.values())
			wallet.put(cur, new BigDecimal(0));
		
		wallet.put(Currency.CHF, new BigDecimal(20_000_000));
	}
	
	public BigDecimal getBalance(Currency cur) {
		return wallet.get(cur);
	}
	
	public void updateWallet(Currency cur, BigDecimal amount) {
		wallet.put(cur, amount);
	}
}