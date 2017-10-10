package io.sutu.warren.DataProcessors.Indicators;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;

public class MACDIndicator extends Indicator {

    private final String name = "MACD";

    public MACDIndicator(TimeSeries timeSeries) {
        this(timeSeries, 12, 24);
    }

    public MACDIndicator(TimeSeries timeSeries, int shortTimeFrame, int longtimeFrame) {
        indicator = new eu.verdelhan.ta4j.indicators.MACDIndicator(new ClosePriceIndicator(timeSeries), shortTimeFrame, longtimeFrame);
    }

    public String getName() {
        return name;
    }
}
