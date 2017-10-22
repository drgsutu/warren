package io.sutu.warren.Trading;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.EMAIndicator;
import eu.verdelhan.ta4j.indicators.MACDIndicator;
import eu.verdelhan.ta4j.indicators.StochasticRSIIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TradingTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TradingTask.class);

    private static final int MINIMUM_TIME_FRAME = 26;

    private String market;
    private BlockingQueue<List<String>> OHLCVQueue;

    TradingTask(
            String market,
            BlockingQueue<List<String>> OHLCVQueue
    ) {
        this.market = market;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(market, ticks);
        timeSeries.setMaximumTickCount(400);

        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        Indicator<Decimal> macd = new MACDIndicator(closePriceIndicator, 12, 26);
        Indicator<Decimal> macdEma = new EMAIndicator(macd, 9);
        Rule entryRule = new CrossedUpIndicatorRule(macd, macdEma);
        Rule exitRule = new CrossedDownIndicatorRule(macd, macdEma);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        StochasticRSIIndicator stochasticRSI = new StochasticRSIIndicator(timeSeries, 14);

        while (!Thread.interrupted()) {
            try {
                List<String> ohlcv = OHLCVQueue.take();

                long timeStamp = Long.parseLong(ohlcv.get(0));
                ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.of("UTC"));
                Tick tick = new BaseTick(endTime, ohlcv.get(1), ohlcv.get(2), ohlcv.get(3), ohlcv.get(4), ohlcv.get(6));
                ticks.add(tick);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (ticks.size() < MINIMUM_TIME_FRAME) {
                    continue;
                }

                logger.debug(DateTimeFormatter.ofPattern("hh:mm").format(tick.getEndTime()) + " " + tick.getClosePrice());

                int endIndex = timeSeries.getEndIndex();
                logger.debug(stochasticRSI.getValue(endIndex).toString());

                if (strategy.shouldEnter(endIndex)) {
                    // buy
                    logger.info("Buy");
                }
                if (strategy.shouldExit(endIndex)) {
                    // sell
                    logger.info("Sell");
                }

            } catch (InterruptedException e) {
                logger.warn("Thread interrupted", e);
            }
        }
    }
}
