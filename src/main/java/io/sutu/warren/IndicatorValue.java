package io.sutu.warren;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public String getIndicatorName() {
        return indicatorName;
    }

    public String getMarket() {
        return market;
    }

    public double getValue() {
        return value;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s][%s] %s - %$f", endTime.format(DateTimeFormatter.ISO_DATE_TIME), market, indicatorName, value
        );
    }
}
