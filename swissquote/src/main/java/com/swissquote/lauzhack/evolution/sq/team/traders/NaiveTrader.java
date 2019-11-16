package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

public class NaiveTrader extends Trader {

	public NaiveTrader(Bank bank) {
		super(bank);
	}

	@Override
	protected void tradeWithBank(BigDecimal diffBase, BigDecimal diffTerm, Currency base, Currency term) {
        
    	if (wallet.add(base, diffBase).getBalance(base).signum() == -1) {
    	    buyToBank(new Trade(base , term, new BigDecimal(1_000_000)));
    	}
	}

	@Override
	public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
	    //NOTHING
	}
	@Override
	public void initialTrade() {
	    //NOTHING
	}
}