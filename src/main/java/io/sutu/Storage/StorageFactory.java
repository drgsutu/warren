package io.sutu.Storage;

import io.sutu.DataProviders.CryptoCompare.MarketData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class StorageFactory {

    private HashMap<String, List<MarketData>> storages = new HashMap<>();

    public List newStorageForMarket(String market) {
        if (!this.storages.containsKey(market)) {
            this.storages.put(market, new ArrayList<>());
        }
        return this.storages.get(market);
    }
}
