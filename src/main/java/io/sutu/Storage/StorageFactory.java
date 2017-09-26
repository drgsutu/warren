package io.sutu.Storage;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

@Component
public class StorageFactory {

    private HashMap<String, Deque<MarketData>> rawStorages = new HashMap<>();

    public Deque<MarketData> newRawStorageForMarket(String market) {
        if (!this.rawStorages.containsKey(market)) {
            this.rawStorages.put(market, new LinkedList<>());
        }
        return this.rawStorages.get(market);
    }

}
