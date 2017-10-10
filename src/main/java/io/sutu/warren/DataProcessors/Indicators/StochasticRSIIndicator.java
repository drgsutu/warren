package io.sutu.warren.DataProcessors.Indicators;

import eu.verdelhan.ta4j.TimeSeries;

public class StochasticRSIIndicator extends Indicator {

    private final String name = "StochasticRSI";

    public StochasticRSIIndicator(TimeSeries timeSeries) {
        this(timeSeries, 14);
    }

    public StochasticRSIIndicator(TimeSeries timeSeries, int timeFrame) {
        indicator = new eu.verdelhan.ta4j.indicators.StochasticRSIIndicator(timeSeries, timeFrame);
    }

    public String getName() {
        return name;
    }
}
