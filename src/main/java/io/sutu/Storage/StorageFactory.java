package io.sutu.Storage;

import io.sutu.DataProviders.Bittrex.BittrexMarkets;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StorageFactory {

    private HashMap<String, Storage> storages= new HashMap<>();

    public Storage newStorageForMarket(String market) {
        switch (market) {
            case BittrexMarkets.BTCETH:
                if (this.storages.containsKey(BittrexMarkets.BTCETH)) {
                    this.storages.put(BittrexMarkets.BTCETH, new BtcEthStorage());
                }
                return this.storages.get(BittrexMarkets.BTCETH);
        }

        return null;
    }
}
