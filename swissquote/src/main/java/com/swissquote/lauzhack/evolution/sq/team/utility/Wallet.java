package com.swissquote.lauzhack.evolution.sq.team.utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.swissquote.lauzhack.evolution.api.Currency;

public final class Wallet {
	
	private final Map<Currency, BigDecimal> wallet;
	private static final BigDecimal WALLET_INITIAL_CHF=new BigDecimal(20_000_000);
	
	public Wallet() {
		this.wallet = new HashMap<>();
		
		for (Currency cur: Currency.values())
			wallet.put(cur, BigDecimal.ZERO);
		
		wallet.put(Currency.CHF, WALLET_INITIAL_CHF);
	}
	
	private Wallet(Map<Currency, BigDecimal> wallet) {
	    this.wallet=new HashMap<Currency, BigDecimal>(wallet);
	}
	
	
	public BigDecimal getBalance(Currency cur) {
		return wallet.get(cur);
	}
	
	public Wallet update(Currency cur, BigDecimal amount) {
	    
	    Map<Currency, BigDecimal> newMap= new HashMap<Currency, BigDecimal>(wallet);
	    newMap.put(cur, wallet.get(cur).add(amount));
		return new Wallet(newMap);
	}
	
}