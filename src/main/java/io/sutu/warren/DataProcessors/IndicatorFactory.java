package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.TimeSeries;
import io.sutu.warren.DataProcessors.Indicators.Indicator;
import io.sutu.warren.DataProcessors.Indicators.MACDIndicator;
import io.sutu.warren.DataProcessors.Indicators.StochasticRSIIndicator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IndicatorFactory {

    public List<Indicator> getIndicators(TimeSeries timeSeries) {
        List<Indicator> indicators = new ArrayList<>();
        indicators.add(new MACDIndicator(timeSeries));
        indicators.add(new StochasticRSIIndicator(timeSeries));

        return indicators;
    }
}
