package io.sutu.DataProcessors;

import com.tictactec.ta.lib.Core;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class IndicatorCalculationTaskFactory {

    private StorageFactory storageFactory;
    private Core core;

    public IndicatorCalculationTaskFactory(StorageFactory storageFactory, Core core) {
        this.storageFactory = storageFactory;
        this.core = core;
    }

    public IndicatorCalculationTask newTaskForMarket(String market) {
        Deque<OHLC> ohlcStorage = storageFactory.newOHLCStorageForMarket(market);
        return new IndicatorCalculationTask(ohlcStorage, core);
    }
}
