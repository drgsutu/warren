package io.sutu.DataTransformers;

import io.sutu.Storage.Storage;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

@Component
public class TickerAggregatorTaskFactory {

    private StorageFactory storageFactory;

    public TickerAggregatorTaskFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public TickerAggregatorTask newTaskForMarket(String market) {
        Storage storage = storageFactory.newStorageForMarket(market);
        return new TickerAggregatorTask(storage);
    }
}
