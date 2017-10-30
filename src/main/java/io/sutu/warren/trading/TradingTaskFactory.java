package io.sutu.warren.trading;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class TradingTaskFactory {

    private TradingStrategiesService tradingStrategiesService;
    private PipelineQueuesFactory pipelineQueuesFactory;

    public TradingTaskFactory(TradingStrategiesService tradingStrategiesService, PipelineQueuesFactory pipelineQueuesFactory) {
        this.tradingStrategiesService = tradingStrategiesService;
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public TradingTask newTaskForMarket() {
        return new TradingTask(tradingStrategiesService, pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
