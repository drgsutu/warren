package io.sutu.DataProviders.Bittrex;

import com.google.gson.Gson;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.Storage.Storage;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

@Component
public class BittrexDataProviderTaskFactory {

    private final BittrexClient bittrexClient;

    private final Gson gson;

    private final StorageFactory storageFactory;

    public BittrexDataProviderTaskFactory(BittrexClient bittrexClient, Gson gson, StorageFactory storageFactory) {
        this.bittrexClient = bittrexClient;
        this.gson = gson;
        this.storageFactory = storageFactory;
    }

    public BittrexDataProviderTask newTaskForMarket(String market) {
        Storage storage = storageFactory.newStorageForMarket(market);
        return new BittrexDataProviderTask(bittrexClient, gson).setStorage(storage).setMarket(market);
    }
}
