package io.sutu.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class IndicatorCalculatorTaskFactory {

    private final BlockingQueue<Tick> aggregatedDataQueue;

    public IndicatorCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.aggregatedDataQueue = pipelineQueuesFactory.getAggregatedDataQueue();
    }

    public IndicatorCalculatorTask newTask() {
        return new IndicatorCalculatorTask(aggregatedDataQueue);
    }
}
