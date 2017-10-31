package io.sutu.warren.trading;

import eu.verdelhan.ta4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TradingStrategiesService {

    private static final Logger logger = LoggerFactory.getLogger(TradingStrategiesService.class);

    private static final int MINIMUM_TIME_FRAME = 26;

    private TimeSeriesService timeSeriesService;
    private final Strategy strategy;

    public TradingStrategiesService(TimeSeriesService timeSeriesService, TradingRulesService tradingRulesService) {
        this.timeSeriesService = timeSeriesService;

        final Rule entryRule = tradingRulesService.getEntryRule();
        final Rule exitRule = tradingRulesService.getExitRule();

        strategy = new BaseStrategy(entryRule, exitRule);
    }

    void addOHLCV(List<String> ohlcv) {
        long timeStamp = Long.parseLong(ohlcv.get(0));
        ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.of("UTC"));
        Tick tick = new BaseTick(endTime, ohlcv.get(1), ohlcv.get(2), ohlcv.get(3), ohlcv.get(4), ohlcv.get(6));

        logger.debug(DateTimeFormatter.ofPattern("hh:mm").format(tick.getEndTime()) + " " + tick.getClosePrice());

        timeSeriesService.update(tick);
    }

    /**
     * Do not calculate until we have the minimum historical data to calculate indicators
     */
    boolean isReadyForTrading() {
        return timeSeriesService.getSize() > MINIMUM_TIME_FRAME;
    }

    boolean shouldEnter() {
        return strategy.shouldEnter(timeSeriesService.getEndIndex());
    }

    boolean shouldExit() {
        return strategy.shouldExit(timeSeriesService.getEndIndex());
    }
}
