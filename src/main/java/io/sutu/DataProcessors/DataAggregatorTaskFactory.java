package io.sutu.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.Trade;
import io.sutu.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class DataAggregatorTaskFactory {

    private BlockingQueue<Trade> rawDataQueue;
    private BlockingQueue<Tick> aggregatedDataQueue;

    public DataAggregatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.rawDataQueue = pipelineQueuesFactory.getRawDataToBeAggregatedQueue();
        this.aggregatedDataQueue = pipelineQueuesFactory.getAggregatedDataQueue();
    }

    public DataAggregatorTask newTask() {
        return new DataAggregatorTask(rawDataQueue, aggregatedDataQueue);
    }
}
