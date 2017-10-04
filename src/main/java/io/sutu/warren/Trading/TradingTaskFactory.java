package io.sutu.warren.Trading;

import io.sutu.warren.IndicatorValue;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class TradingTaskFactory {

    private final BlockingQueue<IndicatorValue> indicatorsValuesQueue;

    public TradingTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        indicatorsValuesQueue = pipelineQueuesFactory.getIndicatorsValuesQueue();
    }

    public TradingTask newTask() {
        return new TradingTask(indicatorsValuesQueue);
    }
}
