package io.sutu.Storage;

import io.sutu.DataProcessors.OHLC;
import io.sutu.DataProviders.CryptoCompare.MarketData;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

@Component
public class StorageFactory {

    private HashMap<String, Deque<MarketData>> rawStorages = new HashMap<>();

    private HashMap<String, Deque<OHLC>> ohlcStorages = new HashMap<>();

    public Deque<MarketData> newRawStorageForMarket(String market) {
        if (!this.rawStorages.containsKey(market)) {
            this.rawStorages.put(market, new LinkedList<>());
        }
        return this.rawStorages.get(market);
    }

    public Deque<OHLC> newOHLCStorageForMarket(String market) {
        if (!this.ohlcStorages.containsKey(market)) {
            this.ohlcStorages.put(market, new LinkedList<>());
        }
        return this.ohlcStorages.get(market);
    }
}
