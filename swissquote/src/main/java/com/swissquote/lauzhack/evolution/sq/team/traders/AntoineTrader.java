package com.swissquote.lauzhack.evolution.sq.team.traders;

import java.math.BigDecimal;

import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

/**
 * @author Antoine
 *
 */
public class AntoineTrader extends Trader {

    private BigDecimal buyQuantity= new BigDecimal(10_000);
    
    /**
     * @param bank
     */
    public AntoineTrader(Bank bank) {
        super(bank);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param term: The currency type you take
     * @param base: The currency type you give
     * @param loss: How much money you lost of base 
     * @param gain: How much money you won of term 
     */
    @Override
    protected void tradeWithBank(BigDecimal baseChange, BigDecimal termChange, Currency base,
            Currency term) {
        
        BigDecimal futureBaseBalance= wallet.getBalance(base).add(baseChange);
        
        while(futureBaseBalance.signum()==-1)
            bank.buy(new Trade(base, term, buyQuantity));
            
        
        
    }

    /* 
     * 
     */
    @Override
    public void tradeWithBankWhenPricesChanges(Price latestChangedPrice) {
        // TODO Auto-generated method stub
        
    }

}
