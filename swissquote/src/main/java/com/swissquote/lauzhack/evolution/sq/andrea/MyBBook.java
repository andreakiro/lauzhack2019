package com.swissquote.lauzhack.evolution.sq.andrea;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;

import javafx.util.Pair;

public class MyBBook implements BBook {

    private final double CHF_RATIO = 0.5;
    private final double OTHER_CURRENCY_RATIO = (1-CHF_RATIO)/4;
    private final double BOUND_DIVERGENCE = 0.5;
    
    // Save a reference to the bank in order to pass orders
    private Bank bank;
    private Map<Currency, BigDecimal> rates = new HashMap<Currency, BigDecimal>();
    private Map<Currency, BigDecimal> markups = new HashMap<Currency, BigDecimal>();
    private Map<Currency, BigDecimal> amounts = new HashMap<Currency, BigDecimal>();

    @Override
    public void onInit() {
    	for(Currency cur : Currency.values())
            amounts.put(cur, BigDecimal.ZERO);
        amounts.put(Currency.CHF, new BigDecimal(20_000_000));

        rates.put(Currency.CHF, BigDecimal.ONE);
        rates.put(Currency.EUR, new BigDecimal(1.09));
        rates.put(Currency.USD, new BigDecimal(0.99));
        rates.put(Currency.GBP, new BigDecimal(1.27));
        rates.put(Currency.JPY, new BigDecimal(0.0091));
        
        markups.put(Currency.EUR, new BigDecimal(0.001));
        markups.put(Currency.USD, new BigDecimal(0.001));
        markups.put(Currency.GBP, new BigDecimal(0.0005));
        markups.put(Currency.JPY, new BigDecimal(0.003));
    }

    @Override
    public void onTrade(Trade trade) {
        
        Pair<BigDecimal, BigDecimal> originalDiffs = calculateClientDiff(trade);
        
        BigDecimal originalGreenDiff = originalDiffs.getKey();
        BigDecimal originalRedDiff = originalDiffs.getValue();
       

        Trade bankTrade;
        BigDecimal bankGreenDiff;
        BigDecimal bankRedDiff;

        BigDecimal moneyAfterClientTrade = amounts.get(trade.base).add(originalRedDiff);
        
        if (moneyAfterClientTrade.subtract(buyThreshold(trade.base)).signum() == -1) {
            bankTrade = new Trade(trade.base, trade.term, restartValue(trade.base).subtract(moneyAfterClientTrade));
            bank.buy(bankTrade);
            
            Pair<BigDecimal, BigDecimal> bankDiffs = calculateBankDiffs(bankTrade);
            bankGreenDiff = bankDiffs.getKey();
            bankRedDiff = bankDiffs.getValue();
            
            amounts.put(bankTrade.base, amounts.get(trade.base).add(bankGreenDiff));
            amounts.put(bankTrade.term, amounts.get(trade.term).add(bankRedDiff));
        }

        amounts.put(trade.term, amounts.get(trade.term).add(originalGreenDiff));
        amounts.put(trade.base, amounts.get(trade.base).add(originalRedDiff));


    }

    @Override
    public void onPrice(Price price) {
        rates.put(price.base, price.rate);
        markups.put(price.base, price.markup);
    }

    @Override
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
    private BigDecimal buyThreshold(Currency cur) {
        return (new BigDecimal(0)).divide(rates.get(cur), 2, RoundingMode.HALF_EVEN);
    }
    
    /**
     * Compute the threshold for a given currency
     * 
     * @param cur: the currency on which we want the threshold
     * @param up: true if we want the upper threshold and false if we want the lower threshold
     * @return the threshold for a given currency.
     */
    private BigDecimal computeThreshold(Currency cur, boolean up) {
        if(up) {
            return restartValue(cur).multiply(BigDecimal.ONE.add(new BigDecimal(BOUND_DIVERGENCE)));
        }
        else {
            return restartValue(cur).multiply(BigDecimal.ONE.subtract(new BigDecimal(BOUND_DIVERGENCE)));
        }
    }

    /**
     * Compute the restartValue for a given currency based on the total amount of money
     * 
     * @param cur: the currency on which we want the restartValue
     * @return the restartValue for a given currency.
     */
    private BigDecimal restartValue(Currency cur) {
        BigDecimal total = totalAmount();
        if (cur == Currency.CHF) {
            return total.multiply(new BigDecimal(CHF_RATIO));
        }
        else {
            return total.multiply(new BigDecimal(OTHER_CURRENCY_RATIO)).divide(rates.get(cur), 2, RoundingMode.HALF_EVEN);

        }
    }
    
    /**
     * This method computes a trade between a client and us.
     * 
     * @param trade: the trade that the client want to do
     * @return a pair consisting of the clientGreenDiff and clientRedDiff
     */
    private Pair<BigDecimal, BigDecimal> calculateClientDiff(Trade trade){
        BigDecimal clientlGreenDiff;
        BigDecimal clientRedDiff;
        if (trade.base == Currency.CHF) {
            clientlGreenDiff = trade.quantity.divide((rates.get(trade.term).multiply(BigDecimal.ONE.subtract(markups.get(trade.term)))), 2, RoundingMode.HALF_EVEN);
            clientRedDiff = trade.quantity.negate().add(BigDecimal.TEN);
        } else {
            clientlGreenDiff = trade.quantity.multiply(rates.get(trade.base)).multiply(BigDecimal.ONE.add(markups.get(trade.base))).add(BigDecimal.TEN);
            clientRedDiff = trade.quantity.negate();
        }
        
        return new Pair<BigDecimal, BigDecimal>(clientlGreenDiff, clientRedDiff);
    }
    
    /**
     * This method computes a trade between us and the bank.
     * 
     * @param trade: the trade that we want to do with the bank
     * @return a pair consisting of the bankGreenDiff and bankRedDiff
     */
    private Pair<BigDecimal, BigDecimal> calculateBankDiffs(Trade trade){
        BigDecimal bankGreenDiff;
        BigDecimal bankRedDiff;
        if (trade.base == Currency.CHF) {
            bankGreenDiff = trade.quantity.subtract(new BigDecimal(100));
            bankRedDiff = trade.quantity.negate().divide((rates.get(trade.term).multiply(BigDecimal.ONE.subtract(markups.get(trade.term)))), 2, RoundingMode.HALF_EVEN);
        } else {
            bankGreenDiff = trade.quantity;
            bankRedDiff = trade.quantity.negate().multiply(rates.get(trade.base)).multiply(BigDecimal.ONE.add(markups.get(trade.base))).subtract(new BigDecimal(100));
        }
        
        return new Pair<BigDecimal, BigDecimal>(bankGreenDiff, bankRedDiff);
    }
    
    /**
     * This method computes the total amount of money we have.
     * 
     * @return the total money we have in CHF.
     */
    private BigDecimal totalAmount() {
        BigDecimal tot = BigDecimal.ZERO;
        for (Currency cur: Currency.values()) {
            tot = tot.add(amounts.get(cur).multiply(rates.get(cur)));
        }   
        return tot;
    }   
}