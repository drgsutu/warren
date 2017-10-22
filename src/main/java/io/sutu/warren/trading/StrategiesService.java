package io.sutu.warren.trading;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.EMAIndicator;
import eu.verdelhan.ta4j.indicators.MACDIndicator;
import eu.verdelhan.ta4j.indicators.StochasticRSIIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class StrategiesService {

    private static final Logger logger = LoggerFactory.getLogger(StrategiesService.class);

    private static final int MINIMUM_TIME_FRAME = 26;

    private final List<Tick> ticks = new ArrayList<>();
    private final TimeSeries timeSeries = new BaseTimeSeries(ticks);
    private final Strategy strategy;

    public StrategiesService() {
        timeSeries.setMaximumTickCount(400);

        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        final Indicator<Decimal> macd = new MACDIndicator(closePriceIndicator, 12, 26);
        final Indicator<Decimal> macdEma = new EMAIndicator(macd, 9);
        final StochasticRSIIndicator stochasticRSI = new StochasticRSIIndicator(timeSeries, 14);

        final Rule macdEntryRule = new CrossedUpIndicatorRule(macd, macdEma);
        final Rule macdExitRule = new CrossedDownIndicatorRule(macd, macdEma);
        final Rule stochasticRSIExitRule = new OverIndicatorRule(stochasticRSI, Decimal.valueOf(80));
        final Rule stochasticRSIEntryRule = new UnderIndicatorRule(stochasticRSI, Decimal.valueOf(20));
        final Rule entryRule = macdEntryRule.and(stochasticRSIEntryRule);
        final Rule exitRule = macdExitRule.and(stochasticRSIExitRule);

        strategy = new BaseStrategy(entryRule, exitRule);
    }

    void addOHLCV(List<String> ohlcv) {
        long timeStamp = Long.parseLong(ohlcv.get(0));
        ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.of("UTC"));
        Tick tick = new BaseTick(endTime, ohlcv.get(1), ohlcv.get(2), ohlcv.get(3), ohlcv.get(4), ohlcv.get(6));

        logger.debug(DateTimeFormatter.ofPattern("hh:mm").format(tick.getEndTime()) + " " + tick.getClosePrice());

        ticks.add(tick);
    }

    /**
     * Do not calculate until we have the minimum historical data to calculate indicators
     */
    boolean isReadyForTrading() {
        return ticks.size() < MINIMUM_TIME_FRAME;
    }

    boolean shouldEnter() {
        return strategy.shouldEnter(timeSeries.getEndIndex());
    }

    boolean shouldExit() {
        return strategy.shouldExit(timeSeries.getEndIndex());
    }
}
