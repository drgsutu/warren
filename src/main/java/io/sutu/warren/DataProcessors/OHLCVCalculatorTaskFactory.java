package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.Trade;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class OHLCVCalculatorTaskFactory {

    private BlockingQueue<Trade> tradesToBeAggregatedQueue;
    private BlockingQueue<Tick> OHLCVQueue;

    public OHLCVCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.tradesToBeAggregatedQueue = pipelineQueuesFactory.getTradesToBeAggregatedQueue();
        this.OHLCVQueue = pipelineQueuesFactory.getOHLCVQueue();
    }

    public OHLCVCalculatorTask newTaskForInterval(int interval) {
        return new OHLCVCalculatorTask(tradesToBeAggregatedQueue, OHLCVQueue, interval);
    }
}
