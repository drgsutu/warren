package io.sutu.warren.trading.indicators;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;

public class StochasticRSIIndicator implements Indicator {

    private static final int TIME_FRAME = 14;
    private static final int LOWER_BOUND = 20;
    private static final int UPPER_BOUND = 80;

    private final eu.verdelhan.ta4j.indicators.StochasticRSIIndicator stochasticRSI;

    public StochasticRSIIndicator(TimeSeries timeSeries) {
        stochasticRSI = new eu.verdelhan.ta4j.indicators.StochasticRSIIndicator(timeSeries, TIME_FRAME);
    }

    @Override
    public Rule getEntryRule() {
        return new UnderIndicatorRule(stochasticRSI, Decimal.valueOf(LOWER_BOUND));
    }

    @Override
    public Rule getExitRule() {
        return new OverIndicatorRule(stochasticRSI, Decimal.valueOf(UPPER_BOUND));
    }
}
