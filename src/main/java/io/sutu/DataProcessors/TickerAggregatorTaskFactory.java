package io.sutu.DataProcessors;

import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TickerAggregatorTaskFactory {

    private StorageFactory storageFactory;

    public TickerAggregatorTaskFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public TickerAggregatorTask newTaskForMarket(String market) {
        List storage = storageFactory.newStorageForMarket(market);
        return new TickerAggregatorTask(storage);
    }
}
