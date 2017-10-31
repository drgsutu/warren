package io.sutu.warren.trading;

import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TimeSeries;
import io.sutu.warren.trading.indicators.Indicator;
import io.sutu.warren.trading.indicators.MACDIndicator;
import io.sutu.warren.trading.indicators.StochasticRSIIndicator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradingRulesService {

    private final List<Indicator> indicators = new ArrayList<>();
    private Rule entryRule;
    private Rule exitRule;

    public TradingRulesService(TimeSeriesService timeSeriesService) {
        TimeSeries timeSeries = timeSeriesService.getTimeSeries();

        // TODO: move indicators selection from code
        this.indicators.add(new MACDIndicator(timeSeries));
        this.indicators.add(new StochasticRSIIndicator(timeSeries));

        buildRules();
    }

    Rule getEntryRule() {
        return entryRule;
    }

    Rule getExitRule() {
        return exitRule;
    }

    private void buildRules() {
        Indicator previousIndicator = null;

        for (Indicator indicator : indicators) {
            if (null == previousIndicator) {
                previousIndicator = indicators.get(0);
                continue;
            }

            entryRule = previousIndicator.getEntryRule().and(indicator.getEntryRule());
            exitRule = previousIndicator.getExitRule().and(indicator.getExitRule());

            previousIndicator = indicator;
        }
    }
}
