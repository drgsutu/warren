package io.sutu.warren.Trading;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class TradingTaskFactory {

    private PipelineQueuesFactory pipelineQueuesFactory;

    public TradingTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public TradingTask newTaskForMarket(String market) {
        return new TradingTask(market, pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
