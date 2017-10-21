package io.sutu.warren.DataProcessors;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class IndicatorCalculatorTaskFactory {

    private PipelineQueuesFactory pipelineQueuesFactory;

    public IndicatorCalculatorTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public IndicatorCalculatorTask newTaskForMarket(String market) {
        return new IndicatorCalculatorTask(market, pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
