package io.sutu.DataProcessors;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class TickerAggregatorTaskFactory {

    private StorageFactory storageFactory;

    public TickerAggregatorTaskFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public TickerAggregatorTask newTaskForMarket(String market) {
        Deque<MarketData> rawDataStorage = storageFactory.newRawStorageForMarket(market);
        Deque<OHLC> ohlcStorage = storageFactory.newOHLCStorageForMarket(market);
        return new TickerAggregatorTask(rawDataStorage, ohlcStorage);
    }
}
