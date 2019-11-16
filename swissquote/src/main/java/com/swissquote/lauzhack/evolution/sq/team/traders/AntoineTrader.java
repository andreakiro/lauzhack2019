package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;

/**
 * @author Antoine
 *
 */
public class AntoineTrader extends Trader {

    /**
     * @param bank
     */
    public AntoineTrader(Bank bank) {
        super(bank);
        // TODO Auto-generated constructor stub
    }

    /* 
     * 
     */
    @Override
    protected void tradeWithBank(BigDecimal loss, BigDecimal gain, Currency base,
            Currency term) {
        
        
    }

    /* 
     * 
     */
    @Override
    public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
        // TODO Auto-generated method stub
        
    }

}
