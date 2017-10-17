package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class IndicatorCalculatorTaskFactory {

    private final BlockingQueue<Tick> OHLCVQueue;

    public IndicatorCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.OHLCVQueue = pipelineQueuesFactory.getOHLCVQueue();
    }

    public IndicatorCalculatorTask newTaskForMarket(String market) {
        return new IndicatorCalculatorTask(market, OHLCVQueue);
    }
}
