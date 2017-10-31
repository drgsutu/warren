package io.sutu.warren.trading.indicators;

import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.EMAIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;

public class MACDIndicator implements Indicator {

    private static final int MACD_SHORT_TIME_FRAME = 12;
    private static final int MACD_LONG_TIME_FRAME = 26;
    private static final int EMA_TIME_FRAME = 9;

    private final eu.verdelhan.ta4j.indicators.MACDIndicator macd;
    private final EMAIndicator macdEma;

    public MACDIndicator(TimeSeries timeSeries) {

        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        macd = new eu.verdelhan.ta4j.indicators.
                MACDIndicator(closePriceIndicator, MACD_SHORT_TIME_FRAME, MACD_LONG_TIME_FRAME);
        macdEma = new EMAIndicator(macd, EMA_TIME_FRAME);
    }

    public Rule getEntryRule() {
        return new CrossedUpIndicatorRule(macd, macdEma);
    }

    public Rule getExitRule() {
        return new CrossedDownIndicatorRule(macd, macdEma);
    }
}
