package io.sutu.warren.DataProcessors.Indicators;

import eu.verdelhan.ta4j.Decimal;

public abstract class Indicator {

    eu.verdelhan.ta4j.Indicator<Decimal> indicator;

    public double getValue(int index) {
        return indicator.getValue(index).toDouble();
    }

    public abstract String getName();
}
