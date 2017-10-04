package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.IndicatorValue;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class IndicatorCalculatorTaskFactory {

    private final BlockingQueue<Tick> aggregatedDataQueue;
    private final BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    public IndicatorCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.aggregatedDataQueue = pipelineQueuesFactory.getAggregatedDataQueue();
        this.indicatorsValuesQueue = pipelineQueuesFactory.getIndicatorsValuesQueue();
    }

    public IndicatorCalculatorTask newTask() {
        return new IndicatorCalculatorTask(aggregatedDataQueue, indicatorsValuesQueue);
    }
}
