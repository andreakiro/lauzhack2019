package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.team.utility.Trader;

public class NaiveTrader extends Trader {

	public NaiveTrader(Bank bank) {
		super(bank);
	}

	@Override
	public void doTrade(Trade trade) {
		// algos malin avec la banque
		
		BigDecimal loss = tradeWithClientLoss(trade.base, trade.term, trade.quantity);
		BigDecimal gain = tradeWithClientGain(trade.base, trade.term, trade.quantity);
		
		updateWallet(trade.base, loss);		
		updateWallet(trade.term, gain);
		
	}

}

//if (Math.random() < 0.05) {
//Trade coverTrade = new Trade(trade.base, trade.term, trade.quantity.multiply(new BigDecimal(2)));
//bank.buy(coverTrade);
//}