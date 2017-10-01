package io.sutu.DataProcessors;

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
        while (true) {
            List<Tick> ticks = new ArrayList<>();

            try {
                Tick tick = aggregatedDataQueue.take();
                System.out.println(tick);
                ticks.add(tick);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int indicatorTimeFrame = 9;
            if (ticks.size() < indicatorTimeFrame) {
                continue;
            }

            TimeSeries timeSeries = new BaseTimeSeries("NEW-BTC", ticks);
            ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);
            SMAIndicator sma = new SMAIndicator(closePrice, indicatorTimeFrame);
            System.out.println(sma);
            System.out.println(sma.getValue(indicatorTimeFrame - 1));
        }
    }
}
