package io.sutu.DataProcessors;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.concurrent.BlockingQueue;

@Component
public class DataAggregatorTaskFactory {

    private StorageFactory storageFactory;

    public DataAggregatorTaskFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public DataAggregatorTask newTaskForMarket(String market) {
        Deque<MarketData> rawDataStorage = storageFactory.newRawStorageForMarket(market);
        Deque<OHLC> ohlcStorage = storageFactory.newOHLCStorageForMarket(market);
        BlockingQueue pipelineState = storageFactory.newPipelinesStatesStoragesForMarket(market);
        return new DataAggregatorTask(market, rawDataStorage, ohlcStorage, pipelineState);
    }
}
