package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.EMAIndicator;
import eu.verdelhan.ta4j.indicators.MACDIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IndicatorCalculatorTask implements Runnable {

    private static final int MINIMUM_TIME_FRAME = 30;

    private String market;
    private BlockingQueue<Tick> OHLCVQueue;

    IndicatorCalculatorTask(
            String market,
            BlockingQueue<Tick> OHLCVQueue
    ) {
        this.market = market;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(market, ticks);
        timeSeries.setMaximumTickCount(400);

        Indicator<Decimal> macd = new MACDIndicator(new ClosePriceIndicator(timeSeries), 12, 24);
        Indicator<Decimal> macdEma = new EMAIndicator(macd, 9);
        Rule entryRule = new CrossedUpIndicatorRule(macdEma, macd);
        Rule exitRule = new CrossedDownIndicatorRule(macdEma, macd);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        while (!Thread.interrupted()) {
            try {
                Tick tick = OHLCVQueue.take();
                ticks.add(tick);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (ticks.size() < MINIMUM_TIME_FRAME) {
                    continue;
                }

                int index = ticks.size() - 1;
                System.out.println(tick);
                Decimal macdValue = macd.getValue(index);
                Decimal macdEmaValue = macdEma.getValue(index);
                System.out.println("MACD " + macdValue);
                System.out.println("EMA  " + macdEmaValue);
                System.out.println("DIFF " + (macdValue.minus(macdEmaValue)));
                if (strategy.shouldEnter(index)) {
                    // buy
                    System.out.println("Buy");
                }
                if (strategy.shouldExit(index)) {
                    // sell
                    System.out.println("Sell");
                }

            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }

        }
    }
}
