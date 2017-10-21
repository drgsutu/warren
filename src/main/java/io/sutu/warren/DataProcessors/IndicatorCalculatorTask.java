package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.EMAIndicator;
import eu.verdelhan.ta4j.indicators.MACDIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IndicatorCalculatorTask implements Runnable {

    private static final int MINIMUM_TIME_FRAME = 25;

    private String market;
    private BlockingQueue<List<String>> OHLCVQueue;

    IndicatorCalculatorTask(
            String market,
            BlockingQueue<List<String>> OHLCVQueue
    ) {
        this.market = market;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        long lastProcessedOHLCVTimeStamp = 0;
        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(market, ticks);
        timeSeries.setMaximumTickCount(400);

        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        Indicator<Decimal> macd = new MACDIndicator(closePriceIndicator, 12, 26);
        Indicator<Decimal> ema = new EMAIndicator(closePriceIndicator, 9);
        Indicator<Decimal> macdEma = new EMAIndicator(macd, 9);
        Rule entryRule = new CrossedUpIndicatorRule(macdEma, macd);
        Rule exitRule = new CrossedDownIndicatorRule(macdEma, macd);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        while (!Thread.interrupted()) {
            try {
                List<String> ohlcv = OHLCVQueue.take();

                long timeStamp = Long.parseLong(ohlcv.get(0));
                if (timeStamp == lastProcessedOHLCVTimeStamp) {
                    continue;
                }
                lastProcessedOHLCVTimeStamp = timeStamp;

                ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.of("UTC"));
                Tick tick = new BaseTick(endTime, ohlcv.get(1), ohlcv.get(2), ohlcv.get(3), ohlcv.get(4), ohlcv.get(5));
                ticks.add(tick);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (ticks.size() < MINIMUM_TIME_FRAME) {
                    continue;
                }

                int index = ticks.size() - 1;
                System.out.println(tick);
                Decimal macdValue = macd.getValue(index);
                Decimal macdEmaValue = macdEma.getValue(index);
                System.out.println(String.format("MACD %.10f", macdValue.toDouble()));
                System.out.println(String.format("MEMA %.10f", macdEmaValue.toDouble()));
                System.out.println(String.format("EMA  %.10f", ema.getValue(index).toDouble()));
                System.out.println(String.format("DIFF %.10f", (macdValue.minus(macdEmaValue)).toDouble()));
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
