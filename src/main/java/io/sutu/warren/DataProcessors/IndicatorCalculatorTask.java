package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.BaseTimeSeries;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.SMAIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IndicatorCalculatorTask implements Runnable {

    private BlockingQueue<Tick> aggregatedDataQueue;

    IndicatorCalculatorTask(BlockingQueue<Tick> aggregatedDataQueue) {
        this.aggregatedDataQueue = aggregatedDataQueue;
    }

    @Override
    public void run() {
        // TODO: move this to a config
        int indicatorTimeFrame = 5;
        // TODO: move this as param
        String timeSeriesName = "NEO-BTC";

        List<Tick> ticks = new ArrayList<>();
        TimeSeries timeSeries = new BaseTimeSeries(timeSeriesName, ticks);
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, indicatorTimeFrame);

        while (!Thread.interrupted()) {
            try {
                Tick tick = aggregatedDataQueue.take();
                System.out.println(tick);
                ticks.add(tick);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }

            // do not calculate until we have the minimum historical data for the indicator calculation
            if (ticks.size() < indicatorTimeFrame) {
                continue;
            }

            System.out.println(sma.getValue(ticks.size() - 1));
        }
    }
}
