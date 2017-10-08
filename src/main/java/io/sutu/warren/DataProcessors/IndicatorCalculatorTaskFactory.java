package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.IndicatorValue;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class IndicatorCalculatorTaskFactory {

    private final BlockingQueue<Tick> OHLCVQueue;
    private final BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    public IndicatorCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.OHLCVQueue = pipelineQueuesFactory.getOHLCVQueue();
        this.indicatorsValuesQueue = pipelineQueuesFactory.getIndicatorsValuesQueue();
    }

    public IndicatorCalculatorTask newTask() {
        return new IndicatorCalculatorTask(OHLCVQueue, indicatorsValuesQueue);
    }
}
