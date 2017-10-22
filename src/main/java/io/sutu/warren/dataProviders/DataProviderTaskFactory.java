package io.sutu.warren.dataProviders;

import io.sutu.warren.communication.kraken.HttpClient;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class DataProviderTaskFactory {

    private HttpClient httpClient;
    private PipelineQueuesFactory pipelineQueuesFactory;

    public DataProviderTaskFactory(HttpClient httpClient, PipelineQueuesFactory pipelineQueuesFactory) {
        this.httpClient = httpClient;
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public DataProviderTask newTaskForMarketAndInterval(String market, int intervalSeconds) {
        return new DataProviderTask(market, intervalSeconds, httpClient, pipelineQueuesFactory.getOHLCVQueues());
    }
}
