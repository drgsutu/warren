package io.sutu.warren;

import eu.verdelhan.ta4j.Tick;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PipelineQueuesFactory {

    private BlockingQueue<Trade> tradesToBeAggregatedQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<Trade> tradesToBeStoredQueue =  new LinkedBlockingQueue<>();
    private List<BlockingQueue<Trade>> tradesQueues = Arrays.asList(tradesToBeAggregatedQueue, tradesToBeStoredQueue);
    private BlockingQueue<Tick> OHLCVQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<IndicatorValue> indicatorsValuesQueue = new LinkedBlockingQueue<>();

    public BlockingQueue<Trade> getTradesToBeAggregatedQueue() {
        return tradesToBeAggregatedQueue;
    }

    public BlockingQueue<Trade> getTradesToBeStoredQueue() {
        return tradesToBeStoredQueue;
    }

    public List<BlockingQueue<Trade>> getTradesQueues() {
        return tradesQueues;
    }

    public BlockingQueue<Tick> getOHLCVQueue() {
        return OHLCVQueue;
    }

    public BlockingQueue<IndicatorValue> getIndicatorsValuesQueue() {
        return indicatorsValuesQueue;
    }
}
