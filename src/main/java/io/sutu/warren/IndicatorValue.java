package io.sutu.warren;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class IndicatorValue {

    private String indicatorName;
    private final double value;
    private final ZonedDateTime endTime;

    public IndicatorValue(String indicatorName, double value, ZonedDateTime endTime) {
        this.indicatorName = indicatorName;
        this.value = value;
        this.endTime = endTime;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public double getValue() {
        return value;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %f", endTime.format(DateTimeFormatter.ISO_DATE_TIME), indicatorName, value);
    }
}
