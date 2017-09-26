package io.sutu.DataProcessors;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class DataAggregatorTaskFactory {

    private StorageFactory storageFactory;

    public DataAggregatorTaskFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public DataAggregatorTask newTaskForMarket(String market) {
        Deque<MarketData> rawDataStorage = storageFactory.newRawStorageForMarket(market);
        Deque<OHLC> ohlcStorage = storageFactory.newOHLCStorageForMarket(market);
        return new DataAggregatorTask(rawDataStorage, ohlcStorage);
    }
}
