package io.sutu.warren.trading;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class TradingTaskFactory {

    private StrategiesService strategiesService;
    private PipelineQueuesFactory pipelineQueuesFactory;

    public TradingTaskFactory(StrategiesService strategiesService, PipelineQueuesFactory pipelineQueuesFactory) {
        this.strategiesService = strategiesService;
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public TradingTask newTaskForMarket() {
        return new TradingTask(strategiesService, pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
