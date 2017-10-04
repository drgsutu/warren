package io.sutu.warren;

import java.time.ZonedDateTime;

public class IndicatorValue {

    private String indicatorName;
    private final String market;
    private final double value;
    private final ZonedDateTime endTime;

    public IndicatorValue(String indicatorName, String market, double value, ZonedDateTime endTime) {
        this.indicatorName = indicatorName;
        this.market = market;
        this.value = value;
        this.endTime = endTime;
    }

}
