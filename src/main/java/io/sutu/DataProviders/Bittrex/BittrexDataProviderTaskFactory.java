package io.sutu.DataProviders.Bittrex;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.Storage;
import org.springframework.stereotype.Component;

@Component
public class BittrexDataProviderTaskFactory {

    private final BittrexClient bittrexClient;

    private final Storage storage;

    public BittrexDataProviderTaskFactory(BittrexClient bittrexClient, Storage storage) {
        this.bittrexClient = bittrexClient;
        this.storage = storage;
    }

    public BittrexDataProviderTask newTaskForMarket(String market) {
        return new BittrexDataProviderTask(bittrexClient, storage).setMarket(market);
    }
}
