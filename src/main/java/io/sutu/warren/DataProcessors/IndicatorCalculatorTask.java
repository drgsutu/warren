package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.BaseTimeSeries;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.SMAIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import io.sutu.warren.IndicatorValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IndicatorCalculatorTask implements Runnable {

    private BlockingQueue<Tick> OHLCVQueue;
    private BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    IndicatorCalculatorTask(
            BlockingQueue<Tick> OHLCVQueue,
            BlockingQueue<IndicatorValue> indicatorsValuesQueue
    ) {
        this.OHLCVQueue = OHLCVQueue;
        this.indicatorsValuesQueue = indicatorsValuesQueue;
    }

    @Override
    public void run() {
        // TODO: move this to a config
        int indicatorTimeFrame = 5;
        // TODO: move this as param
        String market = "NEO-BTC";

        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(market, ticks);
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, indicatorTimeFrame);

        while (!Thread.interrupted()) {
            try {
                Tick tick = OHLCVQueue.take();
                System.out.println(tick);
                ticks.add(tick);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }

            // do not calculate until we have the minimum historical data for the indicator calculation
            if (ticks.size() < indicatorTimeFrame) {
                continue;
            }

            double value = sma.getValue(ticks.size() - 1).toDouble();
            Tick lastTick = ticks.get(ticks.size() - 1);
            IndicatorValue indicatorValue = new IndicatorValue("SMA", market, value, lastTick.getEndTime());
            try {
                indicatorsValuesQueue.put(indicatorValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
