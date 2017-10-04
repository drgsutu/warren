package io.sutu.warren;

import eu.verdelhan.ta4j.Tick;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PipelineQueuesFactory {

    private BlockingQueue<Trade> rawDataToBeAggregatedQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<Trade> rawDataToBeStoredQueue =  new LinkedBlockingQueue<>();
    private List<BlockingQueue<Trade>> rawDataQueues = Arrays.asList(rawDataToBeAggregatedQueue, rawDataToBeStoredQueue);
    private BlockingQueue<Tick> aggregatedDataQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<IndicatorValue> indicatorsValuesQueue = new LinkedBlockingQueue<>();

    public BlockingQueue<Trade> getRawDataToBeAggregatedQueue() {
        return rawDataToBeAggregatedQueue;
    }

    public BlockingQueue<Trade> getRawDataToBeStoredQueue() {
        return rawDataToBeStoredQueue;
    }

    public List<BlockingQueue<Trade>> getRawDataQueues() {
        return rawDataQueues;
    }

    public BlockingQueue<Tick> getAggregatedDataQueue() {
        return aggregatedDataQueue;
    }

    public BlockingQueue<IndicatorValue> getIndicatorsValuesQueue() {
        return indicatorsValuesQueue;
    }
}
