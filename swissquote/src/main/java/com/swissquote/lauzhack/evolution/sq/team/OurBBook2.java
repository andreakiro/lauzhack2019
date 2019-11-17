package com.swissquote.lauzhack.evolution.sq.team;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.swissquote.lauzhack.evolution.api.BBook;
import com.swissquote.lauzhack.evolution.api.Bank;
import com.swissquote.lauzhack.evolution.api.Currency;
import com.swissquote.lauzhack.evolution.api.Price;
import com.swissquote.lauzhack.evolution.api.Trade;
import com.swissquote.lauzhack.evolution.sq.team.traders.NaiveTrader;
import com.swissquote.lauzhack.evolution.sq.team.traders.Trader;

/**
 * This is a very simple example of implementation.
 * This class can be completely discarded.
 */
public class OurBBook2 implements BBook {

    // Save a reference to the bank in order to pass orders
    private Bank bank;
    private Trader trader;
    private Map<Currency, BigDecimal> rates = new HashMap<Currency, BigDecimal>();
    private Map<Currency, BigDecimal> markups = new HashMap<Currency, BigDecimal>();
    private Map<Currency, BigDecimal> amounts = new HashMap<Currency, BigDecimal>();

    private BigDecimal buyThreshold(Currency cur) {
        return (new BigDecimal(0)).divide(rates.get(cur), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal restartValue(Currency cur) {
        return (new BigDecimal(2_000_000)).divide(rates.get(cur), 2, RoundingMode.HALF_EVEN);
    }

    @Override
    public void onInit() {
        for(Currency cur : Currency.values()) {
            amounts.put(cur, BigDecimal.ZERO);
            rates.put(cur, BigDecimal.ONE);
        }
        amounts.put(Currency.CHF, new BigDecimal(20_000_000));
    }

    @Override
    public void onTrade(Trade trade) {
        BigDecimal originalGreenDiff;
        BigDecimal originalRedDiff;
        if (trade.base == Currency.CHF) {
            originalGreenDiff = trade.quantity.divide((rates.get(trade.term).multiply(BigDecimal.ONE.subtract(markups.get(trade.term)))), 2, RoundingMode.HALF_EVEN);
            originalRedDiff = trade.quantity.negate().add(BigDecimal.TEN);
        } else {
            originalGreenDiff = trade.quantity.multiply(rates.get(trade.base)).multiply(BigDecimal.ONE.add(markups.get(trade.base))).add(BigDecimal.TEN);
            originalRedDiff = trade.quantity.negate();
        }

        Trade bankTrade;
        BigDecimal bankGreenDiff;
        BigDecimal bankRedDiff;
        //int i = 0;
//		while (amounts.get(trade.base).add(originalRedDiff).subtract(buyThreshold(trade.base)).signum() == -1) {
//			if (i == 0) {
//				bankTrade = new Trade(trade.base, trade.term, startValue(trade.base));
//			} else {
//				bankTrade = new Trade(bankTrade.base, bankTrade.term, bankTrade.quantity.add(startValue(trade.base)));
//			}
//			bank.buy(bankTrade);
//			if (bankTrade.base == Currency.CHF) {
//				bankGreenDiff = bankTrade.quantity.subtract(new BigDecimal(100));
//				bankRedDiff = bankTrade.quantity.negate().divide((rates.get(bankTrade.term).multiply(BigDecimal.ONE.subtract(markups.get(bankTrade.term)))), 2, RoundingMode.HALF_EVEN);
//			} else {
//				bankGreenDiff = bankTrade.quantity;
//				bankRedDiff = bankTrade.quantity.negate().multiply(rates.get(bankTrade.base)).multiply(BigDecimal.ONE.add(markups.get(bankTrade.base))).subtract(new BigDecimal(100));
//			}
//			amounts.put(bankTrade.base, amounts.get(trade.base).add(bankGreenDiff));
//			amounts.put(bankTrade.term, amounts.get(trade.term).add(bankRedDiff));
//			++i;
//		}
        if (amounts.get(trade.base).add(originalRedDiff).subtract(buyThreshold(trade.base)).signum() == -1) {
            bankTrade = new Trade(trade.base, trade.term, restartValue(trade.base).subtract(amounts.get(trade.base).add(originalRedDiff)));
            bank.buy(bankTrade);
            if (bankTrade.base == Currency.CHF) {
                bankGreenDiff = bankTrade.quantity.subtract(new BigDecimal(100));
                bankRedDiff = bankTrade.quantity.negate().divide((rates.get(bankTrade.term).multiply(BigDecimal.ONE.subtract(markups.get(bankTrade.term)))), 2, RoundingMode.HALF_EVEN);
            } else {
                bankGreenDiff = bankTrade.quantity;
                bankRedDiff = bankTrade.quantity.negate().multiply(rates.get(bankTrade.base)).multiply(BigDecimal.ONE.add(markups.get(bankTrade.base))).subtract(new BigDecimal(100));
            }
            amounts.put(bankTrade.base, amounts.get(trade.base).add(bankGreenDiff));
            amounts.put(bankTrade.term, amounts.get(trade.term).add(bankRedDiff));
        }

        amounts.put(trade.term, amounts.get(trade.term).add(originalGreenDiff));
        amounts.put(trade.base, amounts.get(trade.base).add(originalRedDiff));

        //System.out.println(amounts);


        //trader.clientTradesWithUs(trade);
    }

    @Override
    public void onPrice(Price price) {
        rates.put(price.base, price.rate);
        markups.put(price.base, price.markup);



        //trader.updateMarketPrices(price);
        //trader.tradeWithBankWhenPricesChanges(price);
    }

    @Override
    public void setBank(Bank bank) {
        this.bank = bank;
        //this.trader= new NaiveTrader(bank);
    }
}
