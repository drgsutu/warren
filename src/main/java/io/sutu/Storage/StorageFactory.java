package io.sutu.Storage;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

@Component
public class StorageFactory {

    private HashMap<String, Deque<MarketData>> storages = new HashMap<>();

    public Deque<MarketData> newStorageForMarket(String market) {
        if (!this.storages.containsKey(market)) {
            this.storages.put(market, new LinkedList<>());
        }
        return this.storages.get(market);
    }
}
