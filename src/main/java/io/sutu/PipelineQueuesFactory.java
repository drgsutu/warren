package io.sutu;

import eu.verdelhan.ta4j.Tick;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PipelineQueuesFactory {

    private BlockingQueue<Trade> rawDataToBeAggregatedQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<Trade> rawDataToBeStoredQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<Tick> aggregatedDataQueue =  new LinkedBlockingQueue<>();

    public BlockingQueue<Trade> getRawDataToBeAggregatedQueue() {
        return rawDataToBeAggregatedQueue;
    }

    public BlockingQueue<Trade> getRawDataToBeStoredQueue() {
        return rawDataToBeStoredQueue;
    }

    public BlockingQueue<Tick> getAggregatedDataQueue() {
        return aggregatedDataQueue;
    }
}
