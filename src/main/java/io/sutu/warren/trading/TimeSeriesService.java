package io.sutu.warren.trading;

import eu.verdelhan.ta4j.BaseTimeSeries;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeSeriesService {

    private final List<Tick> ticks = new ArrayList<>();
    private final TimeSeries timeSeries = new BaseTimeSeries(ticks);

    public TimeSeriesService() {
        timeSeries.setMaximumTickCount(400);
    }

    void update(Tick tick) {
        ticks.add(tick);
    }

    int getSize() {
        return ticks.size();
    }

    int getEndIndex() {
        return timeSeries.getEndIndex();
    }

    public TimeSeries getTimeSeries() {
        return timeSeries;
    }
}
