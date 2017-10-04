package io.sutu.warren.Trading;

import io.sutu.warren.IndicatorValue;

import java.util.concurrent.BlockingQueue;

public class TradingTask implements Runnable {

    private BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    TradingTask(BlockingQueue<IndicatorValue> indicatorsValuesQueue) {
        this.indicatorsValuesQueue = indicatorsValuesQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                IndicatorValue indicatorValue = indicatorsValuesQueue.take();
                System.out.println(indicatorValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
