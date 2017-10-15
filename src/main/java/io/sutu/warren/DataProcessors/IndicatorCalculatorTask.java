package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.BaseTimeSeries;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import io.sutu.warren.DataProcessors.Indicators.Indicator;
import io.sutu.warren.IndicatorValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IndicatorCalculatorTask implements Runnable {

    private static final int MINIMUM_TIME_FRAME = 30;

    private String market;
    private IndicatorFactory indicatorFactory;
    private BlockingQueue<Tick> OHLCVQueue;
    private BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    IndicatorCalculatorTask(
            String market,
            IndicatorFactory indicatorFactory,
            BlockingQueue<Tick> OHLCVQueue,
            BlockingQueue<IndicatorValue> indicatorsValuesQueue
    ) {
        this.market = market;
        this.indicatorFactory = indicatorFactory;
        this.OHLCVQueue = OHLCVQueue;
        this.indicatorsValuesQueue = indicatorsValuesQueue;
    }

    @Override
    public void run() {
        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(market, ticks);
        List<Indicator> indicators = indicatorFactory.getIndicators(timeSeries);

        while (!Thread.interrupted()) {
            try {
                Tick tick = OHLCVQueue.take();
                ticks.add(tick);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (ticks.size() < MINIMUM_TIME_FRAME) {
                    continue;
                }

                for (Indicator indicator : indicators) {
                    double value = indicator.getValue(ticks.size() - 1);
                    IndicatorValue indicatorValue = new IndicatorValue(indicator.getName(), value, tick.getEndTime());

                    try {
                        indicatorsValuesQueue.put(indicatorValue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(tick);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }

        }
    }
}
